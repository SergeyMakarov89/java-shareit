package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentDtoResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemServiceImpl itemService;

    @Test
    void testCreateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item1");
        itemDto.setDescription("ItemDescription1");
        itemDto.setAvailable(true);

        when(itemService.createItem(any(Long.class), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void testUpdateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("Item2");
        itemDto.setDescription("ItemDescription2");
        itemDto.setAvailable(true);

        when(itemService.updateItem(any(Long.class), any(Long.class), any(ItemDto.class))).thenReturn(itemDto);

        mvc.perform(patch("/items/" + 2L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void testDeleteItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(3L);
        itemDto.setName("Item3");
        itemDto.setDescription("ItemDescription3");
        itemDto.setAvailable(true);
        itemService.createItem(1L, itemDto);

        mvc.perform(delete("/items/" + 3L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testGetItemById() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(4L);
        itemDto.setName("Item4");
        itemDto.setDescription("ItemDescription4");
        itemDto.setAvailable(true);
        itemService.createItem(1L, itemDto);

        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();
        itemDtoResponse.setId(4L);
        itemDtoResponse.setName("Item4");
        itemDtoResponse.setDescription("ItemDescription4");
        itemDtoResponse.setAvailable(true);

        when(itemService.getItemById(any(Long.class))).thenReturn(itemDtoResponse);

        mvc.perform(get("/items/" + 4L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.name", is(itemDtoResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoResponse.getDescription())));
    }

    @Test
    void testGetItemsByUserId() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(5L);
        itemDto.setName("Item5");
        itemDto.setDescription("ItemDescription5");
        itemDto.setAvailable(true);
        itemService.createItem(1L, itemDto);

        when(itemService.getItemsByUserId(any(Long.class))).thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())));
    }

    @Test
    void testFindItems() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(6L);
        itemDto.setName("Item6");
        itemDto.setDescription("ItemDescription6");
        itemDto.setAvailable(true);
        itemService.createItem(1L, itemDto);

        when(itemService.findItems(any(String.class))).thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "Descript")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())));
    }

    @Test
    void testCreateComment() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(7L);
        itemDto.setName("Item7");
        itemDto.setDescription("ItemDescription7");
        itemDto.setAvailable(true);
        itemService.createItem(1L, itemDto);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("CommentText1");

        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();
        commentDtoResponse.setId(1L);
        commentDtoResponse.setText("CommentText1");

        when(itemService.createComment(any(Long.class), any(Long.class), any(CommentDto.class))).thenReturn(commentDtoResponse);

        mvc.perform(post("/items/" + 7L + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(commentDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.text", is(commentDtoResponse.getText())));
    }
}
