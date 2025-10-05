import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    public ConsoleUI(UserService userService) {
        this.userService = userService;
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Your choice: ");
            switch (choice) {
                case 1 -> createUser();
                case 2 -> getUserById();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 5 -> showAllUsers();
                case 0 -> exit();
                default -> log.warn("Invalid choice.");
            }
        }
    }

    private void printMenu() {
        log.info("\nChoose option:");
        log.info("1. Create user");
        log.info("2. Get user by ID");
        log.info("3. Update user");
        log.info("4. Delete user");
        log.info("5. Show all users");
        log.info("0. Exit");
    }

    private void createUser() {
        try {
            String name = readLine("Enter name: ");
            String email = readLine("Enter email: ");
            int age = readInt("Enter age: ");
            userService.createUser(name, email, age);
        } catch (Exception e) {
            log.error("Failed to create user", e);
        }
    }

    private void getUserById() {
        try {
            long id = readLong("Enter user ID: ");
            userService.getUser(id);
        } catch (Exception e) {
            log.error("Failed to retrieve user", e);
        }
    }

    private void updateUser() {
        try {
            long id = readLong("Enter user ID to update: ");
            String name = readLine("New name: ");
            String email = readLine("New email: ");
            int age = readInt("New age: ");
            userService.updateUser(id, name, email, age);
        } catch (Exception e) {
            log.error("Failed to update user", e);
        }
    }

    private void deleteUser() {
        try {
            long id = readLong("Enter user ID to delete: ");
            userService.deleteUser(id);
        } catch (Exception e) {
            log.error("Failed to delete user", e);
        }
    }

    private void showAllUsers() {
        try {
            userService.showAllUsers();
        } catch (Exception e) {
            log.error("Failed to display users", e);
        }
    }

    private void exit() {
        log.info("Exit.");
        System.exit(0);
    }

    private String readLine(String prompt) {
        log.info(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                log.warn("Invalid number. Try again.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                log.warn("Invalid number. Try again.");
            }
        }
    }
}
