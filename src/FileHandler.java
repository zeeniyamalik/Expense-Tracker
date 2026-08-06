import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE_NAME = "expenses.txt";

    // Saving - append
    public static void saveExpense(Expense e) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    e.getAmount() + "," +
                            e.getCategory() + "," +
                            e.getDate() + "," +
                            e.getNote() + "\n"
            );
        } catch (IOException ex) {
            System.out.println("Error saving expense to file.");
        }
    }
    public static void saveAllExpenses(ArrayList<Expense> expenses) {
        try (FileWriter writer = new FileWriter(FILE_NAME, false)) { // false = overwrite whole file
            for (Expense e : expenses) {
                writer.write(
                        e.getAmount() + "," +
                                e.getCategory() + "," +
                                e.getDate() + "," +
                                e.getNote() + "\n"
                );
            }
        } catch (IOException ex) {
            System.out.println("Error saving expenses to file.");
        }
    }

    // Loading
    public static ArrayList<Expense> loadExpenses() {
        ArrayList<Expense> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {

                    LocalDate date = LocalDate.parse(parts[2]);

                    Expense e = new Expense(
                            parts[1],                       // category
                            Double.parseDouble(parts[0]),   // amount
                            date,                           // date
                            parts[3]                        // note
                    );
                    list.add(e);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }

        return list;
    }
}

