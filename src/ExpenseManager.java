import java.util.*;
public class ExpenseManager {

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

}
