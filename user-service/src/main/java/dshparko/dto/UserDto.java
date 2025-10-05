package dshparko.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserDto(Long id,
                      @NotBlank(message = "Name must not be blank")
                      String name,
                      @NotBlank(message = "Email must not be blank")
                      @Email(message = "Email should be valid")
                      String email,
                      @Min(value = 0, message = "Age must be non-negative")
                      int age,
                      LocalDate createdAt) {
}
