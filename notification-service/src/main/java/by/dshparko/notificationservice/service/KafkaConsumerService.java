package by.dshparko.notificationservice.service;

import by.dshparko.notificationservice.model.UserEvent;
import by.dshparko.notificationservice.util.NotificationMessageBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}")
    public void handle(String message) throws Exception {
        var event = objectMapper.readValue(message, UserEvent.class);
        emailService.send(event);
    }
}
