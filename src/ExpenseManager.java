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

}
