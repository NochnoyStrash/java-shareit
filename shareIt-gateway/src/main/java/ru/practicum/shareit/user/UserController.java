package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Creating;
import ru.practicum.shareit.Updating;
import ru.practicum.shareit.item.dto.Validation;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        return userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Validated(value = Creating.class) UserDto user) {
        return userClient.addUser(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @NotNull Long userId) {
        return userClient.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable long userId,
                                            @RequestBody @Validated(value = Updating.class) UserDto user) {
        Validation.user(user);
        return userClient.patchUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long userId) {
        return userClient.deleteUser(userId);
    }
}
