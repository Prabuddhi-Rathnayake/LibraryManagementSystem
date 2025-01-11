package com.librarymanagementsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Library library = new Library();

        // Load data from files
        List<LibraryItem> libraryItems = LibraryIO.loadItemsFromFile("itemlist.lms");
        for (LibraryItem item : libraryItems) {
            library.addItem(item);
        }

        List<User> users = LibraryIO.loadUserListFromFile("userlist.lms");
        for (User user : users) {
            library.addUser(user);
        }

        Map<String, String> borrowedItems = LibraryIO.loadBorrowedItemsFromFile("borroweditems.lms");
        for (Map.Entry<String, String> borrowedItem : borrowedItems.entrySet()) {
            library.getBorrowedItems().put(borrowedItem.getKey(), borrowedItem.getValue());
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Need to create a new item");
            System.out.println("2. Need to create a new User");
            System.out.println("3. User need to borrow an item");
            System.out.println("4. User need to return an item");
            System.out.println("5. Exit the application");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int option;
            try {
                option = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }

            switch (option) {
                case 1:
                    createNewItem(library);
                    break;
                case 2:
                    createNewUser(library);
                    break;
                case 3:
                    borrowItem(library);
                    break;
                case 4:
                    returnItem(library);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        // Save data back to files
        LibraryIO.saveItemToFile(library.getLibraryItems(), "itemlist.lms");
        LibraryIO.saveUserListToFile(library.getUserList(), "userlist.lms");
        LibraryIO.saveBorrowedItemsToFile(library.getBorrowedItems(), "borroweditems.lms");
        System.out.println("Application exited and data saved.");
    }

    private static void createNewItem(Library library) throws IOException {
        System.out.println("Which item do you need to create?");
        System.out.println("1. Book");
        System.out.println("2. Magazine");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = Integer.parseInt(reader.readLine());

        if (choice == 1) {
            System.out.println("Enter book title:");
            String title = reader.readLine();
            System.out.println("Enter book author:");
            String author = reader.readLine();
            System.out.println("Enter book serial number:");
            String serialNumber = reader.readLine();

            for (LibraryItem item : library.getLibraryItems()) {
                if (Objects.equals(item.getSerialNumber(), serialNumber)) {
                    System.out.println("This serial number is already used!");
                    return;
                }
            }

            LibraryItem book = new Book(title, author, serialNumber);
            library.addItem(book);
            System.out.println("Book added successfully.");
        } else if (choice == 2) {
            System.out.println("Enter magazine title:");
            String title = reader.readLine();
            System.out.println("Enter magazine author:");
            String author = reader.readLine();
            System.out.println("Enter magazine serial number:");
            String serialNumber = reader.readLine();

            for (LibraryItem item : library.getLibraryItems()) {
                if (Objects.equals(item.getSerialNumber(), serialNumber)) {
                    System.out.println("This serial number is already used!");
                    return;
                }
            }

            LibraryItem magazine = new Magazine(title, author, serialNumber);
            library.addItem(magazine);
            System.out.println("Magazine added successfully.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void createNewUser(Library library) throws IOException {
        System.out.println("Enter the name of the new user:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String userName = reader.readLine();

        for (User user : library.getUserList()) {
            if (Objects.equals(user.getName(), userName)) {
                System.out.println("User already exists.");
                return;
            }
        }

        User newUser = new User(userName);
        library.addUser(newUser);
        System.out.println("User created successfully.");
    }

    private static void borrowItem(Library library) throws IOException {
        System.out.println("Enter the serial number of the item to borrow:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String serialNumber = reader.readLine();

        System.out.println("Enter the user's name:");
        String userName = reader.readLine();

        User borrower = null;
        for (User user : library.getUserList()) {
            if (Objects.equals(user.getName(), userName)) {
                borrower = user;
                break;
            }
        }

        if (borrower == null) {
            System.out.println("User not found.");
            return;
        }

        library.borrowItem(serialNumber, borrower);
    }

    private static void returnItem(Library library) throws IOException {
        System.out.println("Enter the serial number of the item to return:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String serialNumber = reader.readLine();

        System.out.println("Enter the user's name:");
        String userName = reader.readLine();

        User returner = null;
        for (User user : library.getUserList()) {
            if (Objects.equals(user.getName(), userName)) {
                returner = user;
                break;
            }
        }

        if (returner == null) {
            System.out.println("User not found.");
            return;
        }

        library.returnBorrowedItem(serialNumber, returner);
    }
}
