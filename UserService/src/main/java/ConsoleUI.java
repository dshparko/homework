import java.util.Scanner;

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
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nChoose option:");
        System.out.println("1. Create user");
        System.out.println("2. Get user by ID");
        System.out.println("3. Update user");
        System.out.println("4. Delete user");
        System.out.println("5. Show all users");
        System.out.println("0. Exit");
    }

    private void createUser() {
        String name = readLine("Enter name: ");
        String email = readLine("Enter email: ");
        int age = readInt("Enter age: ");
        userService.createUser(name, email, age);
    }

    private void getUserById() {
        long id = readLong("Enter user ID: ");
        userService.getUser(id);
    }

    private void updateUser() {
        long id = readLong("Enter user ID to update: ");
        String name = readLine("New name: ");
        String email = readLine("New email: ");
        int age = readInt("New age: ");
        userService.updateUser(id, name, email, age);
    }

    private void deleteUser() {
        long id = readLong("Enter user ID to delete: ");
        userService.deleteUser(id);
    }

    private void showAllUsers() {
        userService.showAllUsers();
    }

    private void exit() {
        System.out.println("Exit.");
        System.exit(0);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}
