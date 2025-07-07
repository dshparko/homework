package dshparko;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
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
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public UserDto createUser(UserDto dto) {
        User user = repository.save(mapper.toEntity(dto));
        return mapper.toDto(user);

    }

    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        return mapper.toDto(repository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
