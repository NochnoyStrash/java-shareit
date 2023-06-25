package ru.practicum.shareit.user.service;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UsersRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    UserServiceImpl userService;
    @Mock
    UsersRepository usersRepository;
    EasyRandom generator = new EasyRandom();
    User user;

    @BeforeEach
    public void beforeEach() {
        userService = new UserServiceImpl(usersRepository);
        user = generator.nextObject(User.class);
    }


    @Test
    void getUser() {
        Mockito
                .when(usersRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        UserNotFoundException e = assertThrows(UserNotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.getUser(1L);
            }
        });
        assertEquals("Пользователь с ID = 1 отсутствует", e.getMessage());
    }

    @Test
    void updateUser() {
        User user1 = generator.nextObject(User.class);
        Mockito
                .when(usersRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito
                .when(usersRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        User user2 = userService.updateUser(1L, user1);
        Mockito
                .verify(usersRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
        Mockito
                .verify(usersRepository, Mockito.times(1))
                .save(Mockito.any(User.class));

    }

    @Test
    public void deleteUser() {
        userService.deleteUser(1);
        Mockito.verify(usersRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());

    }

    @Test
    void getAllUsers() {
        userService.getAllUsers();
        Mockito
                .verify(usersRepository, Mockito.times(1))
                .findAll();
    }
}