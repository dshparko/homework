package dshparko;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody @Valid UserDto dto) {
        return service.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


