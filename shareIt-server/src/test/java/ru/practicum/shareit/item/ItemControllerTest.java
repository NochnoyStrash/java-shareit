package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoLastAndNextBooking;
import ru.practicum.shareit.item.exception.CommentsValidateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    EasyRandom generator = new EasyRandom();
    private static final String USER_ID_HEADERS = "X-Sharer-User-Id";

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    void getItemTest() throws Exception {
        ItemDtoLastAndNextBooking idt = generator.nextObject(ItemDtoLastAndNextBooking.class);
        Mockito
                .when(itemService.getItemLastNext(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(idt);

        mvc.perform(get("/items/1")
                .header(USER_ID_HEADERS, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idt.getId()));

    }

    @Test
    void addItemTest() throws Exception {
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        Mockito
                .when(itemService.addItem(Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                .header(USER_ID_HEADERS, "1")
                .content(objectMapper.writeValueAsString(itemDto))
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()));

    }

    @Test
    void patchItemTest() throws Exception {
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        Item item = generator.nextObject(Item.class);
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        Mockito
                .when(itemService.updateItem(Mockito.any(ItemDto.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .header(USER_ID_HEADERS, "1")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void getItemMasterTest() throws Exception {
        List<ItemDtoLastAndNextBooking> bookings = new ArrayList<>();
        ItemDtoLastAndNextBooking gh = generator.nextObject(ItemDtoLastAndNextBooking.class);
        ItemDtoLastAndNextBooking gg = generator.nextObject(ItemDtoLastAndNextBooking.class);
        bookings.add(gh);
        bookings.add(gg);

        Mockito
                .when(itemService.findItemWithBookingLastNext(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(bookings);

        mvc.perform(get("/items")
                        .header(USER_ID_HEADERS, "1")
                        .param("from", "1")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(gh.getId()))
                .andExpect(jsonPath("$[1].id").value(gg.getId()));


    }

    @Test
    void searchItemTest() throws Exception {
        ItemDto itemDto = generator.nextObject(ItemDto.class);
        ItemDto itemDto1 = generator.nextObject(ItemDto.class);
        List<ItemDto> itemDtos = List.of(itemDto, itemDto1);

        Mockito
                .when(itemService.findItemFromText(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(itemDtos);

        mvc.perform(get("/items/search")
                        .header(USER_ID_HEADERS, "1")
                .param("text", "отверт")
                .param("from", "1")
                .param("size", "2")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        Comment comment = generator.nextObject(Comment.class);
        comment.setId(commentDto.getId());
        comment.getAuthor().setName(commentDto.getAuthorName());
        Mockito
                .when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenReturn(comment);
        Mockito
                .when(itemService.getComment(Mockito.any(Comment.class)))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_ID_HEADERS, "1")
                .content(objectMapper.writeValueAsString(commentDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));

    }

    @Test
    public void addCommentWithExceptionTest() throws Exception {
        CommentDto commentDto = generator.nextObject(CommentDto.class);
        Mockito
                .when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenThrow(CommentsValidateException.class);

        mvc.perform(post("/items/1/comment")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(USER_ID_HEADERS, "1")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));


    }

}