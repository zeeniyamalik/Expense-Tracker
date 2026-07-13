import java.time.Month;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.YearMonth;

public class ExpenseManager {
    Scanner sc = new Scanner(System.in);
    public double monthlyBudget = 0.0;

   private ArrayList<Expense> expenses = FileHandler.loadExpenses();

   public void addExpense(Expense e){
        expenses.add(e);
       FileHandler.saveExpense(e);
   }

    public void viewAllExpenses(){
       if(expenses.isEmpty()){
           System.out.println("No Expenses Found.");
           return;
       }
        for(Expense e : expenses){
            System.out.println(e.toString());
        }
    }

    public void addMonthlyBudget(){
        monthlyBudget = sc.nextDouble();
        System.out.println("Monthly Budget: " + monthlyBudget + "\tSaved Successfully!");
    }

    public void burnRateWarning(){

        double spentSoFar = 0;
        LocalDate today = LocalDate.now();
        int daysPassed = today.getDayOfMonth();
        int daysInMonth = today.lengthOfMonth();

       for(Expense e : expenses){
           if(e.getDate().getMonth() == today.getMonth() && e.getDate().getYear() == today.getYear()){
               spentSoFar += e.getAmount();
           }
       }

        double dailyAvg = spentSoFar / daysPassed;
        double projectedTotal = dailyAvg * daysInMonth;

        System.out.println("Total spent so far this month: " + spentSoFar);

        if (projectedTotal > monthlyBudget) {
            int dayBudgetRunsOut = (int)(monthlyBudget / dailyAvg);
            System.out.println("WARNING: At this rate, you'll exceed your budget of " + monthlyBudget + " by day " + dayBudgetRunsOut + " of the month :(");
        } else {
            System.out.println("Good Job, You're on track! Projected total: " + projectedTotal + ", staying under your " + monthlyBudget + " budget :)");
        }

    }
    public void showSummary(){
        if(expenses.isEmpty()){
            System.out.println("No Expenses Found.");
            return;
        }
        HashMap<String, Double> categoryTotals = new HashMap<>();
        for (Expense e : expenses) {
            if(categoryTotals.containsKey(e.getCategory())){
                categoryTotals.put(e.getCategory(), categoryTotals.get(e.getCategory()) + e.getAmount());
            } else{
                categoryTotals.put(e.getCategory(), e.getAmount());
            }
        }

        for(String cat : categoryTotals.keySet()){
            System.out.println(cat + " -> " + categoryTotals.get(cat));
        }
        burnRateWarning();
    }

    public void NextMonthForecast(){
       //if the total months are < 3 then its inapplicable
        LocalDate today = LocalDate.now();
        //calcualtin total per month per category
        HashMap<String, HashMap<YearMonth, Double>> categoryMonthTotals = new HashMap<>();
        for(Expense e : expenses){
            String category = e.getCategory();
            YearMonth expenseMonth = YearMonth.from(e.getDate());

            if(!categoryMonthTotals.containsKey(category)){
                categoryMonthTotals.put(category, new HashMap<>());
            }

            HashMap<YearMonth, Double> monthMap = categoryMonthTotals.get(category);

            if(monthMap.containsKey(expenseMonth)){
                monthMap.put(expenseMonth, monthMap.get(expenseMonth) + e.getAmount());
            }else {
                monthMap.put(expenseMonth, e.getAmount());
            }

        }
        // >= 3 months check and final forecast

        for(String category : categoryMonthTotals.keySet()){
            HashMap<YearMonth, Double> monthMap = categoryMonthTotals.get(category);

            if(monthMap.size() < 3){
                System.out.println("For " + category + ": Not enough data yet. (Need at least 3 months of data)");
            }else{
                ArrayList<YearMonth> months = new ArrayList<>(monthMap.keySet());
                Collections.sort(months);

                int size = months.size();
                YearMonth m1 = months.get(size - 1);
                YearMonth m2 = months.get(size - 2);
                YearMonth m3 = months.get(size - 3);

                //total
                double total = monthMap.get(m1) + monthMap.get(m2) + monthMap.get(m3);
                double forecast = total/3;

                System.out.println("For " + category + ": Based on your last 3 months, you'll likely spend ~" + forecast + " next month.");

            }
        }


    }

}
