package by.dshparko.notificationservice;

import by.dshparko.notificationservice.model.UserEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JavaMailSender javaMailSender;

    void performNotificationTest(String operationType, String email) throws Exception {
        var event = new
                UserEvent(operationType, email);
        var json = objectMapper.writeValueAsString(event);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());
        verify(javaMailSender).send((MimeMessage) org.mockito.Mockito.any());
    }

    @Test
    void testEmailSendingWithCreate() throws Exception {
        performNotificationTest("CREATE", "test@example.com");
    }

    @Test
    void testEmailSendingWithUpdate() throws Exception {
        performNotificationTest("UPDATE", "test@example.com");
    }

    @Test
    void testEmailSendingWithDelete() throws Exception {
        performNotificationTest("DELETE", "test@example.com");
    }
}