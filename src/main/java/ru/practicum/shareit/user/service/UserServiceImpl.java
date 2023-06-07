package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public User addUser(User user) {
        return usersRepository.save(user);
    }

    @Override
    public User getUser(long userId) {
        return  usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с ID = %d отсутствует", userId)));
    }

    @Override
    @Transactional
    public User patchUser(long userId, User user) {
        User user1 = usersRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь с ID = %d отсутствует", userId)));
        if (user.getName() != null) {
            user1.setName(user.getName());
        }
        if (user.getEmail() != null) {
            user1.setEmail(user.getEmail());
        }

        return usersRepository.save(user1);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        usersRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }
}
