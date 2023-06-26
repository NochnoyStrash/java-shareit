package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    RequestService requestService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    EasyRandom generator = new EasyRandom();

    @Test
    void addRequest() {
    }

    @Test
    void getRequestsFromUser() throws Exception {
        ItemRequest itemRequest = generator.nextObject(ItemRequest.class);
        List<ItemRequest> list = Collections.singletonList(itemRequest);

        Mockito
                .when(requestService.getRequestFromUser(Mockito.anyLong()))
                .thenReturn(list);
        mvc.perform(get("/requests")
                .header("X-Sharer-User-Id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

    }

    @Test
    void getAllRequest() {
    }

    @Test
    void getRequest() {
    }
}