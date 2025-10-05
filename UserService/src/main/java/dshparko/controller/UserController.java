package dshparko.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dshparko.dto.UserDto;
import dshparko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDto> all() {
        return service.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto byId(@PathVariable("id") Long id) {
        return service.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Validated UserDto dto) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody @Validated UserDto dto) throws JsonProcessingException {
        return service.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws JsonProcessingException {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


