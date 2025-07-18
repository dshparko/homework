package dshparko.mapper;

import dshparko.dto.UserDto;
import dshparko.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.id())
                .name(dto.name())
                .email(dto.email())
                .age(dto.age())
                .createdAt(LocalDate.now())
                .build();
    }
}
