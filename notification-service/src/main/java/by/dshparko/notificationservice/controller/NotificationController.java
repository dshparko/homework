package by.dshparko.notificationservice.controller;

import by.dshparko.notificationservice.model.UserEvent;
import by.dshparko.notificationservice.service.EmailService;
import by.dshparko.notificationservice.util.NotificationMessageBuilder;
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
        emailService.send(event);
        return ResponseEntity.ok().build();
    }
}

