package dshparko;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserDto> findAllUsers() {
        try {
            return repository.findAll().stream().map(mapper::toDto).toList();
        } catch (Exception e) {
            log.error("Error retrieving users: " + e.getMessage());
            throw new RuntimeException("Error retrieving users: ", e);
        }
    }

    public UserDto getUser(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserDto createUser(UserDto dto) {
        try {
            User user = repository.save(mapper.toEntity(dto));
            return mapper.toDto(user);
        } catch (Exception e) {
            log.error("Error occurred while creating user: " + e.getMessage());
            throw new RuntimeException("Error occurred while creating user", e);
        }
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        return mapper.toDto(repository.save(user));
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
