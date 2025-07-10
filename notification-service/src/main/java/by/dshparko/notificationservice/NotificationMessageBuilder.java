package by.dshparko.notificationservice;

import org.springframework.stereotype.Component;

@Component
public class NotificationMessageBuilder {

    public String build(String type) {
        return switch (type.toUpperCase()) {
            case "CREATE" -> "Hello! Your account was successfully created.";
            case "UPDATE" -> "Hello! Your account was successfully updated.";
            case "DELETE" -> "Hello! Your account was successfully deleted.";
            default       -> "Incorrect parameters";
        };
    }
}
