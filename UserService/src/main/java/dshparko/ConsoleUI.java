package dshparko;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Deprecated
@RequiredArgsConstructor
@Slf4j
public class ConsoleUI {
    private final UserService userService;

    private final Scanner scanner = new Scanner(System.in);

    @PostConstruct
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
            UserDto dto = new UserDto(null, name, email, age, LocalDate.now());
            userService.createUser(dto);
            log.info("User is created: " + dto);
        } catch (Exception e) {
            log.error("Failed to create user", e);
        }
    }

    private void getUserById() {
        try {
            long id = readLong("Enter user ID: ");
            var user = userService.getUser(id);
            log.info("User: " + user);
        } catch (Exception e) {
            log.error("Failed to retrieve user", e);
        }
    }

    private void updateUser() {
        try {
            Long id = readLong("Enter user ID to update: ");
            userService.getUser(id);

            String name = readLine("New name: ");
            String email = readLine("New email: ");
            int age = readInt("New age: ");
            UserDto user = userService.getUser(id);
            UserDto dto = new UserDto(id, name, email, age, user.createdAt());
            UserDto updated = userService.updateUser(id, dto);
            log.info("User was updated " + updated);
        } catch (Exception e) {
            log.error("Failed to update user", e);
        }
    }

    private void deleteUser() {
        try {
            long id = readLong("Enter user ID to delete: ");
            userService.deleteUser(id);
            log.info("User with id {} was deleted ", id);
        } catch (Exception e) {
            log.error("Failed to delete user", e);
        }
    }

    private void showAllUsers() {
        try {
            List<UserDto> users = userService.findAllUsers();
            log.info("Users: ");
            users.forEach(user -> log.info(user.toString()));
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
