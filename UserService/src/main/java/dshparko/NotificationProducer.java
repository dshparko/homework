package dshparko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserEvent(String type, String email) throws JsonProcessingException {
        try {
            UserEvent event = new UserEvent(type, email);
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("user-events", message);
        } catch (Exception e) {
            throw e;
        }
    }
}
