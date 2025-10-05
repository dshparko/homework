package dshparko.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Operations for managing users: creating, receiving, updating and deleting")
public class UserController {
    private final UserService service;

    @Operation(summary = "Get a list of all users", description = "Returns a collection of users with HATEOAS links")
    @ApiResponse(responseCode = "200", description = "User list successfully returned")
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

    @Operation(summary = "Get user by ID", description = "Returns one user with HATEOAS links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was found"),
            @ApiResponse(responseCode = "404", description = "User wasn't found")
    })
    @GetMapping("/{id}")
    public EntityModel<UserDto> byId(@PathVariable("id")
                                     @Parameter(description = "ID of the user to retrieve", example = "1")
                                     Long id) throws JsonProcessingException {
        UserDto dto = service.getUser(id);

        return EntityModel.of(dto,
                linkTo(methodOn(UserController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).update(id, dto)).withRel("update"),
                linkTo(methodOn(UserController.class).delete(id)).withRel("delete"),
                linkTo(methodOn(UserController.class).all()).withRel("all-users"));
    }

    @Operation(summary = "Create a new user", description = "Accepts DTO and creates user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User was created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> create(@RequestBody(
            description = "User data: \"name\" - Username (required), \"email\" - User email (valid), \"age\" - User age (>= 0)"
    )
                                                       @Validated UserDto dto) throws JsonProcessingException {
        UserDto created = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                EntityModel.of(created,
                        linkTo(methodOn(UserController.class).byId(created.id())).withSelfRel(),
                        linkTo(methodOn(UserController.class).all()).withRel("all-users"))
        );
    }

    @Operation(summary = "Refresh user", description = "Updates existing user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User was successfully updated"),
            @ApiResponse(responseCode = "404", description = "User wasn't found")
    })
    @PutMapping("/{id}")
    public EntityModel<UserDto> update(@PathVariable("id") Long id, @RequestBody @Validated UserDto dto) throws JsonProcessingException {
        UserDto updated = service.updateUser(id, dto);
        return EntityModel.of(updated,
                linkTo(methodOn(UserController.class).byId(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("all-users"));
    }

    @Operation(summary = "Delete a user", description = "Remove user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User was successfully removed"),
            @ApiResponse(responseCode = "404", description = "User wasn't found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id")
                                       @Parameter(description = "ID of the user to delete", example = "1")
                                       Long id) throws JsonProcessingException {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
