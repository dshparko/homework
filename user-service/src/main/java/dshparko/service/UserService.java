package dshparko.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dshparko.model.User;
import dshparko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dshparko.dto.UserDto;
import dshparko.exception.UserNotFoundException;
import dshparko.mapper.UserMapper;
import dshparko.producer.NotificationProducer;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final NotificationProducer notificationProducer;


    public List<UserDto> findAllUsers() {

        return repository.findAll().stream().map(mapper::toDto).toList();

    }

    public UserDto getUser(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public UserDto createUser(UserDto dto) throws JsonProcessingException {
        User user = repository.save(mapper.toEntity(dto));

        notificationProducer.sendUserEvent("CREATE", user.getEmail());

        return mapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto dto) throws JsonProcessingException {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());

        notificationProducer.sendUserEvent("UPDATE", user.getEmail());
        return mapper.toDto(repository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) throws JsonProcessingException {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        User user = optionalUser.get();
        repository.deleteById(id);

        notificationProducer.sendUserEvent("DELETE", user.getEmail());
    }
}
