package dshparko.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dshparko.dto.UserEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic}")
    private String topic;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserEvent(String type, String email) throws JsonProcessingException {
        try {
            UserEvent event = new UserEvent(type, email);
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            throw e;
        }
    }
}
