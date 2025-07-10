package by.dshparko.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final NotificationMessageBuilder messageBuilder;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void handle(String message) throws Exception {
        var event = objectMapper.readValue(message, UserEvent.class);
        var text = messageBuilder.build(event.getType());
        emailService.send(event.getEmail(), "Notification", text);
    }
}
