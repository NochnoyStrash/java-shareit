package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    RequestService requestService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    EasyRandom generator = new EasyRandom();
    ItemRequest itemRequest;
    ItemRequestDto itemRequestDto;

    private static final String USER_ID_HEADERS = "X-Sharer-User-Id";

    @BeforeEach
    public void beforeEach() {
        itemRequest = generator.nextObject(ItemRequest.class);
        itemRequestDto = generator.nextObject(ItemRequestDto.class);

        List<ItemRequest> list = Collections.singletonList(itemRequest);
        Mockito
                .when(requestService.addRequest(Mockito.any(ItemRequestDto.class), Mockito.anyLong()))
                .thenReturn(itemRequest);

        Mockito
                .when(requestService.getRequestFromUser(Mockito.anyLong()))
                .thenReturn(list);

        Mockito
                .when(requestService.getRequestDto(itemRequest))
                .thenReturn(itemRequestDto);
        Mockito
                .when(requestService.getAllRequests(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));
        Mockito
                .when(requestService.getItemRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequest);

    }

    @Test
    void addRequest() throws Exception {
        mvc.perform(post("/requests")
                .header(USER_ID_HEADERS, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));
    }

    @Test
    void getRequestsFromUser() throws Exception {
        mvc.perform(get("/requests")
                .header(USER_ID_HEADERS, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()));

    }

    @Test
    void getAllRequestTest() throws Exception {
        mvc.perform(get("/requests/all")
                .header(USER_ID_HEADERS, "1")
                .param("from", "1")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()));
    }

    @Test
    void getRequestTest() throws Exception {
        mvc.perform(get("/requests/1")
                .header(USER_ID_HEADERS, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description")
                        .value(itemRequestDto.getDescription()));
    }
}