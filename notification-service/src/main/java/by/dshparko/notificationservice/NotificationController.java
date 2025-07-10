package by.dshparko.notificationservice;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;
    private final NotificationMessageBuilder messageBuilder;

    @PostMapping
    public ResponseEntity<Void> notifyUser(@RequestBody UserEvent event) throws MessagingException {
        emailService.send(event.getEmail(),
                "Notification",
                messageBuilder.build(event.getType()));
        return ResponseEntity.ok().build();
    }
}

