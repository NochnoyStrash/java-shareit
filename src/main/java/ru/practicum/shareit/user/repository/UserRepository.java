package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    User getUser(long userId);

    User addUser(User user);

    User patchUser(User user, long userid);

    void deleteUser(long userId);

    List<User> getAll();
}
