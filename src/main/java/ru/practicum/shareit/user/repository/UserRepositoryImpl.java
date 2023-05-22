package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserValidateException;

import java.util.*;

@Repository
public class UserRepositoryImpl implements  UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long indetificate = 1;

    @Override
    public User getUser(long userId) {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
        return userMap.get(userId);
    }

    @Override
    public User addUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new UserValidateException("Пользователь с таким Email уже существует");
        }
        user.setId(indetificate++);
        emails.add(user.getEmail());
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public User patchUser(User user, long userId) {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
        User user1 = getUser(userId);
        if (emails.contains(user.getEmail())) {
            if (!Objects.equals(user1.getEmail(), user.getEmail())) {
                throw new UserValidateException("Пользователь с таким Email уже существует");
            }
        }

        if (user.getName() != null) {
            user1.setName(user.getName());
        }
        if (user.getEmail() != null) {
            emails.remove(user1.getEmail());
            user1.setEmail(user.getEmail());
            emails.add(user1.getEmail());
        }
        return user1;
    }

    @Override
    public void deleteUser(long userId) {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException("Пользователь с таким id = " + userId + " не найден!");
        }
        emails.remove(userMap.get(userId).getEmail());
        userMap.remove(userId);

    }

    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
