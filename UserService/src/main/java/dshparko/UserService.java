package dshparko;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserDto> findAllUsers() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    public UserDto getUser(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow();
    }

    public UserDto createUser(UserDto dto) {
        User user = repository.save(mapper.toEntity(dto));
        return mapper.toDto(user);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id).orElseThrow();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        return mapper.toDto(repository.save(user));
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
