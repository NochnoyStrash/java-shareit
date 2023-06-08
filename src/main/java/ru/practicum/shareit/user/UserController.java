package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@PathVariable long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

}
