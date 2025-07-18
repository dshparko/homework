package dshparko.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import dshparko.dto.UserDto;
import dshparko.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public CollectionModel<EntityModel<UserDto>> all() {
        List<EntityModel<UserDto>> users = service.findAllUsers().stream()
                .map(user -> {
                    try {
                        return EntityModel.of(user,
                                linkTo(methodOn(UserController.class).byId(user.id())).withSelfRel(),
                                linkTo(methodOn(UserController.class).all()).withRel("all-users"));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<UserDto> byId(@PathVariable("id") Long id) throws JsonProcessingException {
        UserDto dto = service.getUser(id);

        return EntityModel.of(dto,
                linkTo(methodOn(UserController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).update(id, dto)).withRel("update"),
                linkTo(methodOn(UserController.class).delete(id)).withRel("delete"),
                linkTo(methodOn(UserController.class).all()).withRel("all-users"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> create(@RequestBody @Validated UserDto dto) throws JsonProcessingException {
        UserDto created = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                EntityModel.of(created,
                        linkTo(methodOn(UserController.class).byId(created.id())).withSelfRel(),
                        linkTo(methodOn(UserController.class).all()).withRel("all-users"))
        );
    }

    @PutMapping("/{id}")
    public EntityModel<UserDto> update(@PathVariable("id") Long id, @RequestBody @Validated UserDto dto) throws JsonProcessingException {
        UserDto updated = service.updateUser(id, dto);
        return EntityModel.of(updated,
                linkTo(methodOn(UserController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("all-users"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws JsonProcessingException {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
