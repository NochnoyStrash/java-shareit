package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User getUser(long userid);

    User patchUser(long userId, User user);

    void deleteUser(long user);

    List<User> getAllUsers();
}
