import org.w3c.dom.ls.LSOutput;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();
        System.out.println("Welcome to Expense Tracker!");

        while(true){
            System.out.println("Choose:");
            System.out.println("1. Add an Expense\n2. View All Expenses\n3. Show Summary of Expenses\n4. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();

            if(choice==1) {
                while (true) {
                    String[] categories = {"Food", "Travel", "Bills", "Shopping", "Entertainment", "Extra" };
                    System.out.println("ADDING YOUR EXPENSE\nSelect category:");
                    for (int i = 0; i < categories.length; i++) {
                        System.out.println((i + 1) + ". " + categories[i]);
                    }
                    int catChoice = sc.nextInt();
                    sc.nextLine();

                    String selectedCategory = categories[catChoice - 1];

                    //amount
                    System.out.println("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();

                    //date
                    System.out.println("Enter date (DD-MM-YYYY): ");
                    String dateInput = sc.nextLine();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate date = LocalDate.parse(dateInput, formatter);


                    //note
                    System.out.println("Enter note:");
                    String note = sc.nextLine();

                    //add expense
                    Expense newExpense = new Expense(selectedCategory, amount, date, note);
                    manager.addExpense(newExpense);
                    System.out.println("Want to add another? (Y/N) ");
                    String more = sc.nextLine();
                    if (more.equalsIgnoreCase("n")) {
                        break;
                    }
                }
            }
             else if(choice == 2){
                System.out.println("Showing All Expenses:");
                manager.viewAllExpenses();
            }
            else if(choice == 3){
                System.out.println("Showing Summary of All Expenses:");
                manager.showSummary();
            }
            else if(choice == 4){
                System.out.println("Exiting....");
                break;
            }
            else System.out.println("Invalid Selection! Try Again.");
        }
    }
}
