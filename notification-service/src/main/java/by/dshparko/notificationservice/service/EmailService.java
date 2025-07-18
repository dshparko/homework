package by.dshparko.notificationservice.service;

import by.dshparko.notificationservice.model.UserEvent;
import by.dshparko.notificationservice.util.NotificationMessageBuilder;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private final NotificationMessageBuilder messageBuilder;

    @Value("${notification.subject}")
    private String subject;

    public void send(UserEvent event) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(event.getEmail());
        helper.setSubject(subject);
        helper.setText(messageBuilder.build(event.getType()), false);
        mailSender.send(message);
    }
}

