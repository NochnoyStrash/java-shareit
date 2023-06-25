package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    EasyRandom generator = new EasyRandom();
    User user;
    User user1;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "Ваня", "rondo@mail.ru");
        user1 = new User(2L, "Марк", "tima@mail.ru");


    }

    @Test
    void getAllUser() throws Exception {
        List<User> users = List.of(user1, user);
        Mockito
                .when(userService.getAllUsers())
                .thenReturn(users);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[1].id").value(user.getId()));
    }

    @Test
    void addUser() throws Exception {
        Mockito
                .when(userService.addUser(Mockito.any(User.class)))
                .thenReturn(user);

        mvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(user))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ваня"));
    }

    @Test
    void getUser() throws Exception {
        Mockito
                .when(userService.getUser(Mockito.anyLong()))
                .thenReturn(user);
        mvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));


    }

    @Test
    void patchUser() throws Exception {
        Mockito
                .when(userService.updateUser(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(user1);
        mvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(user1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user1.getEmail()));

    }

//    @Test
//    void deleteUser() throws Exception {
//        mvc.perform(patch("/users/1")
//                .content(objectMapper.writeValueAsString(1))
//                .characterEncoding(StandardCharsets.UTF_8)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON));
//        Mockito
//                .verify(userService, Mockito.times(1))
//                .deleteUser(1);
//
//    }

    @Test
    void getUserWithException() throws Exception {
        Mockito
                .when(userService.getUser(Mockito.anyLong()))
                .thenThrow(UserNotFoundException.class);
        mvc.perform(get("/users/1")
                        .content(objectMapper.writeValueAsString(1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));


    }


}