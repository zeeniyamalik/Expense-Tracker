package com.zeeniya.expense_tracker.ai;

import com.zeeniya.expense_tracker.dto.BurnRateWarning;
import com.zeeniya.expense_tracker.dto.CategorySummary;
import com.zeeniya.expense_tracker.dto.ExpenseRequest;
import com.zeeniya.expense_tracker.dto.ForecastResponse;
import com.zeeniya.expense_tracker.dto.ai.ExpenseParseResponse;
import com.zeeniya.expense_tracker.dto.ai.GeminiResponse;
import com.zeeniya.expense_tracker.dto.ai.InsightResponse;
import com.zeeniya.expense_tracker.entity.Expense;
import com.zeeniya.expense_tracker.entity.User;
import com.zeeniya.expense_tracker.exception.MultipleExpensesDetectedException;
import com.zeeniya.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final String DEFINITIONS = """
            Definitions:
            - Monthly budget: the amount the user plans to spend this month.
            - Spent so far: total spent this month up to today.
            - Daily average: spent so far divided by days passed.
            - Required daily average: remaining budget divided by remaining days. This is
              the most the user can spend per day to stay within budget.
            - Projected total: what they will have spent by month end if they keep
              spending at the actual daily average.
            - Burn rate: how fast the budget is being used. If the actual daily average
              is higher than the required one, they are on track to overspend.
            - Spending share: the percentage of total spending each category takes.
            - Forecast: the expected spending per category next month, based on the
              past 3 months.
            - Unusual expense: a single expense much larger than the user's usual ones.
              It can distort the daily average, so mention it separately from categories.
            """;

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService;

    public GeminiService(RestClient restClient, ObjectMapper objectMapper, ExpenseService expenseService) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
        this.expenseService = expenseService;
    }

    // Expense parsing

    public ExpenseParseResponse parseExpense(String userInput) {
        String prompt = """
                You are an expense parser for a personal finance app backend.
                
                Extract expense details from the user's input and output ONLY a single valid JSON object.
                
                Output format (exact keys, exact types):
                {
                    "category": string,
                    "amount": number,
                    "date": "YYYY-MM-DD",
                    "note": string or null,
                    "multipleDetected": boolean
                }
                
                Rules:
                1. Category: pick the closest match from this list: Food, Travel, Bills, Shopping, Entertainment, Extra.
                   Only create a new category if the expense truly does not fit any of them (e.g. "Medical", "Education").
                   Do not invent a new category just because the wording differs ("groceries" still maps to Food).
                   Use Title Case for any new category.
                2. Amount: a plain positive number, no currency symbols, no commas, no text.
                   If a range is given ("500-600"), use the higher number.
                   If the amount is missing or unclear, use 0.
                3. Date: always YYYY-MM-DD. If no date is mentioned, use today's date.
                   If a relative date is mentioned ("yesterday", "last Friday", "2 days ago"), calculate it relative to today.
                   If a date has no year, assume the current year.
                4. Note: optional. Only add something useful beyond category and amount, like a specific item or merchant
                   ("dinner with friends", "Uber to airport"). If there's nothing worth adding, set it to null.
                   Never repeat the category or amount inside the note.
                5. multipleDetected: set to true if the input clearly describes more than one separate expense
                   (e.g. "bought groceries for 500 and paid 2000 electricity bill"). Otherwise false.
                   When true, still extract only the first expense into category/amount/date/note.
                
                Edge cases:
                - If the input has nothing to do with an expense, still return valid JSON:
                  category "Extra", amount 0, date today, note briefly explaining the input was unclear, multipleDetected false.
                
                Today's date is: %s
                
                User input:
                %s
                """.formatted(LocalDate.now(), userInput);

        Map<String, Object> schema = Map.of(
                "type", "OBJECT",
                "properties", Map.of(
                        "category", Map.of("type", "STRING"),
                        "amount", Map.of("type", "NUMBER"),
                        "date", Map.of("type", "STRING"),
                        "note", Map.of("type", "STRING", "nullable", true),
                        "multipleDetected", Map.of("type", "BOOLEAN")
                ),
                "required", List.of("category", "amount", "date", "multipleDetected")
        );

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseSchema", schema
                )
        );

        String rawJson = callGemini(requestBody);

        try {
            return objectMapper.readValue(rawJson, ExpenseParseResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response into ExpenseParseResponse", e);
        }
    }


    // Spending insights

    public InsightResponse getSpendingInsights(User currentUser) {
        BurnRateWarning burnRate = expenseService.getBurnRate(currentUser);
        List<CategorySummary> categories = expenseService.getCategorySummary(currentUser);
        List<ForecastResponse> forecast = expenseService.getForecast(currentUser);

        // no expenses, no point calling Gemini
        if (categories.isEmpty()) {
            return insightMessage("Add some expenses first, then I can suggest what to avoid.");
        }

        StringBuilder data = new StringBuilder();
        data.append("BURN RATE\n");
        if (burnRate.isBudgetSet()) {
            data.append("Monthly budget: ").append(burnRate.getMonthlyBudget()).append("\n");
            data.append("Spent so far: ").append(burnRate.getSpentSoFar()).append("\n");
            data.append("Actual daily average: ").append(burnRate.getDailyAverage()).append("\n");
            data.append("Required daily average: ").append(burnRate.getRequiredDailyAverage()).append("\n");
            data.append("Projected total: ").append(burnRate.getProjectedTotal()).append("\n");
            data.append("Over budget pace: ").append(burnRate.isOverBudget()).append("\n");
        } else data.append("No monthly budget has been set.\n");

        if (burnRate.isHasAnomaly()) {
            data.append("Unusual expense: ").append(burnRate.getAnomalyMessage()).append("\n");
        }

        data.append("\nSPENDING SHARE BY CATEGORY (percent of total)\n");
        for (CategorySummary c : categories) {
            data.append(c.getCategory()).append(": ").append(c.getPercentage()).append("\n");
        }

        data.append("\nFORECAST NEXT MONTH BY CATEGORY\n");
        for (ForecastResponse f : forecast) {
            data.append(f.getCategory()).append(": ").append(f.getForecast()).append("\n");
        }

        String prompt = """
            You are a personal finance assistant inside an expense tracker.

            Rules:
            - Only use the numbers given below. Do not invent any figures.
            - Use plain, simple language. Keep every reason to one sentence.
            - If there is not enough data for a useful tip (for example no budget is set),
              return an empty "avoid" list and explain in "summary" what is missing.

            """
                + DEFINITIONS +
                """

            Task:
            Using the definitions above, look at the burn rate, spending share and forecast.
            Suggest which types of expenses the user should avoid next, and why.
            Give 1 to 3 items.

            Return ONLY valid JSON in this exact shape, with no extra text and no
            markdown fences:
            {
              "summary": "one or two sentences about their overall situation",
              "avoid": [
                { "category": "category name", "reason": "why", "severity": "low, medium or high" }
              ]
            }

            Data:
            """ + data;

        Map<String, Object> schema = Map.of(
                "type", "OBJECT",
                "properties", Map.of(
                        "summary", Map.of("type", "STRING"),
                        "avoid", Map.of(
                                "type", "ARRAY",
                                "items", Map.of(
                                        "type", "OBJECT",
                                        "properties", Map.of(
                                                "category", Map.of("type", "STRING"),
                                                "reason", Map.of("type", "STRING"),
                                                "severity", Map.of("type", "STRING")
                                        ),
                                        "required", List.of("category", "reason", "severity")
                                )
                        )
                ),
                "required", List.of("summary", "avoid")
        );

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseSchema", schema
                )
        );

        try {
            String rawJson = callGemini(requestBody);
            return objectMapper.readValue(rawJson, InsightResponse.class);
        } catch (AiUnavailableException e) {
            return insightMessage(e.getMessage());
        } catch (Exception e) {
            return insightMessage("Could not generate insights right now. Please try again.");
        }
    }

    // Helpers

    private String callGemini(Map<String, Object> requestBody) {
        GeminiResponse response;
        try {
            response = restClient
                    .post()
                    .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent")
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(GeminiResponse.class);
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new AiUnavailableException("AI service is busy right now, try again shortly, or add this expense manually.");
        }

        return response.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    private InsightResponse insightMessage(String message) {
        InsightResponse response = new InsightResponse();
        response.setSummary(message);
        response.setAvoid(new ArrayList<>());
        return response;
    }

    public Expense parseAndSaveExpense(String userInput, User currentUser) {
        ExpenseParseResponse parsed = parseExpense(userInput);

        if (parsed.isMultipleDetected()) {
            throw new MultipleExpensesDetectedException(
                    "It looks like you mentioned more than one expense. Please add them one at a time."
            );
        }

        if (parsed.getAmount() <= 0) {
            throw new IllegalArgumentException("Could not detect a valid amount from your input.");
        }

        ExpenseRequest request = new ExpenseRequest(
                parsed.getCategory(),
                BigDecimal.valueOf(parsed.getAmount()),
                LocalDate.parse(parsed.getDate()),
                parsed.getNote()
        );

        return expenseService.addExpense(request, currentUser);
    }
}
