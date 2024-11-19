import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {
    private static final Scanner in = new Scanner(System.in);

    private static ArrayList<String> list = new ArrayList<>();
    private static boolean needsToBeSaved = false; // Dirty flag to track changes

    public static void main(String[] args) {
        boolean quit = false;
        String filename = null; // Tracks the current list file name

        while (!quit) {
            System.out.println("\nMenu:");
            System.out.println("A - Add an item to the list");
            System.out.println("D - Delete an item from the list");
            System.out.println("I - Insert an item into the list");
            System.out.println("M - Move an item");
            System.out.println("V - View the list");
            System.out.println("O - Open a list file from disk");
            System.out.println("S - Save the current list file to disk");
            System.out.println("C - Clear the list");
            System.out.println("Q - Quit");
            System.out.print("Enter your choice: ");
            String choice = in.nextLine().toUpperCase();

            switch (choice) {
                case "A" -> addItem();
                case "D" -> deleteItem();
                case "I" -> insertItem();
                case "M" -> moveItem();
                case "V" -> viewList();
                case "O" -> filename = openFile(filename);
                case "S" -> saveFile(filename);
                case "C" -> clearList();
                case "Q" -> quit = quitProgram(filename);
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Add an item to the list
    private static void addItem() {
        System.out.print("Enter the item to add: ");
        String item = in.nextLine();
        list.add(item);
        needsToBeSaved = true;
        System.out.println("Item added.");
    }

    // Delete an item from the list
    private static void deleteItem() {
        viewList();
        System.out.print("Enter the index of the item to delete: ");
        int index = getIndex();
        if (isValidIndex(index)) {
            list.remove(index);
            needsToBeSaved = true;
            System.out.println("Item deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    // Insert an item into the list at a specific position
    private static void insertItem() {
        viewList();
        System.out.print("Enter the index to insert at: ");
        int index = getIndex();
        if (isValidIndex(index) || index == list.size()) {
            System.out.print("Enter the item to insert: ");
            String item = in.nextLine();
            list.add(index, item);
            needsToBeSaved = true;
            System.out.println("Item inserted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    // Move an item from one position to another
    private static void moveItem() {
        viewList();
        System.out.print("Enter the index of the item to move: ");
        int fromIndex = getIndex();
        if (isValidIndex(fromIndex)) {
            System.out.print("Enter the new index: ");
            int toIndex = getIndex();
            if (isValidIndex(toIndex) || toIndex == list.size()) {
                String item = list.remove(fromIndex);
                list.add(toIndex, item);
                needsToBeSaved = true;
                System.out.println("Item moved.");
            } else {
                System.out.println("Invalid destination index.");
            }
        } else {
            System.out.println("Invalid source index.");
        }
    }

    // View the current list
    private static void viewList() {
        System.out.println("\nCurrent List:");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d: %s%n", i, list.get(i));
        }
    }

    // Open a list file from disk
    private static String openFile(String currentFilename) {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before opening a new file? (Y/N): ");
            if (getYesOrNo()) {
                saveFile(currentFilename);
            }
        }
        System.out.print("Enter the filename to open: ");
        String filename = in.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            list.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            needsToBeSaved = false;
            System.out.println("File loaded successfully.");
            return filename;
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
            return currentFilename;
        }
    }

    // Save the current list to disk
    private static void saveFile(String filename) {
        if (filename == null) {
            System.out.print("Enter a filename to save the list: ");
            filename = in.nextLine();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String item : list) {
                writer.println(item);
            }
            needsToBeSaved = false;
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // Clear the current list
    private static void clearList() {
        list.clear();
        needsToBeSaved = true;
        System.out.println("List cleared.");
    }

    // Quit the program, prompting to save unsaved changes
    private static boolean quitProgram(String filename) {
        if (needsToBeSaved) {
            System.out.print("You have unsaved changes. Save before quitting? (Y/N): ");
            if (getYesOrNo()) {
                saveFile(filename);
            }
        }
        System.out.println("Goodbye!");
        return true;
    }

    // Utility: Get a valid integer index
    private static int getIndex() {
        try {
            return Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Utility: Check if an index is valid for the current list
    private static boolean isValidIndex(int index) {
        return index >= 0 && index < list.size();
    }

    // Utility: Get a Yes or No response from the user
    private static boolean getYesOrNo() {
        String response = in.nextLine().toUpperCase();
        return response.startsWith("Y");
    }
}

