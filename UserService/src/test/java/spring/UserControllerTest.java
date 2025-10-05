package spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;

import dshparko.App;
import dshparko.controller.UserController;
import dshparko.dto.UserDto;
import dshparko.exception.UserNotFoundException;
import dshparko.service.UserService;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = App.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final int TEST_VAR = 1;

    @Test
    @DisplayName("GET /api/users → should return all users")
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> users = List.of(new UserDto(1L, "Darya", "darya@mail.ru", 22, LocalDate.now()));
        when(service.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Darya"))
                .andExpect(jsonPath("$[0].email").value("darya@mail.ru"))
                .andExpect(jsonPath("$[0].age").value(22));
    }

    @Test
    @DisplayName("GET /api/users/{id} → should return user by ID")
    void shouldReturnUserById() throws Exception {
        UserDto user = new UserDto(1L, "Darya", "darya@mail.ru", 22, LocalDate.now());
        when(service.getUser(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", TEST_VAR))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Darya"))
                .andExpect(jsonPath("$.email").value("darya@mail.ru"));
    }

    @Test
    @DisplayName("POST /api/users → should create user")
    void shouldCreateUser() throws Exception {
        UserDto input = new UserDto(null, "Ivan", "ivan@yandex.ru", 30, null);
        UserDto saved = new UserDto(2L, "Ivan", "ivan@yandex.ru", 30, LocalDate.now());
        when(service.createUser(Mockito.any(UserDto.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.email").value("ivan@yandex.ru"));
    }

    @Test
    @DisplayName("PUT /api/users/{id} → should update user")
    void shouldUpdateUser() throws Exception {
        UserDto input = new UserDto(null, "Updated", "updated@example.com", 28, null);
        UserDto updated = new UserDto(1L, "Updated", "updated@example.com", 28, LocalDate.now());
        when(service.updateUser(Mockito.eq(1L), Mockito.any(UserDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/{id}", TEST_VAR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → should delete user")
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", TEST_VAR))
                .andExpect(status().isNoContent());
        Mockito.verify(service).deleteUser(1L);
    }

    @Test
    @DisplayName("404 Not Found")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(service.getUser(100L)).thenThrow(new UserNotFoundException(100L));

        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User with ID " + 100 + " wasn't found"));
    }

    @Test
    @DisplayName("400 Bad Request")
    void shouldReturn400WhenValidationFails() throws Exception {
        UserDto invalidDto = new UserDto(null, "", "invalid", -1, null);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation was failed"));
    }

    @Test
    @DisplayName("400 Invalid Request Body")
    void shouldReturn400WhenJsonIsInvalid() throws Exception {
        String invalidJson = "{\"id\": null, \"name\": \"Test\", \"email\": , }";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid request body"));
    }


    @Test
    @DisplayName("500 Internal Server Error")
    void shouldReturn500OnUnexpectedException() throws Exception {
        when(service.findAllUsers()).thenThrow(new RuntimeException("DB crashed"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal error"));
    }
}
