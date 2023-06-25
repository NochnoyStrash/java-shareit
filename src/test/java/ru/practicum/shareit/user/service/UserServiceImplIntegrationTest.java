package ru.practicum.shareit.user.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UsersRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplIntegrationTest {
    @Autowired
    UserService userService;
    EasyRandom generator = new EasyRandom();

    @Test
    void updateUserTestIntegration() {
        User user = new User(null, "weru", "qwerty@mail.ru");
        user = userService.addUser(user);
        User user1 = new User(user.getId(), "more", "tutu@mail.ru");
        User user2 = userService.updateUser(user.getId(), user1);
        assertEquals(user2.getId(), user1.getId());
        assertEquals(user2.getName(), user1.getName());
        assertEquals(user2.getEmail(), user1.getEmail());

        UserNotFoundException e = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                User user3 = userService.updateUser(77, user1);
            }
        });
        assertEquals("Пользователь с ID = 77 отсутствует", e.getMessage());


    }
}