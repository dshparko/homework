package dshparko;

import java.time.LocalDate;

public record UserDto(Long id, String name, String email, int age, LocalDate createdAt) {
}
