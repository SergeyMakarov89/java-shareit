package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void testCreateItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("itemRequestDtoDescription1");

        ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse();
        itemRequestDtoResponse.setId(1L);
        itemRequestDtoResponse.setDescription("itemRequestDtoDescription1");

        when(itemRequestService.createItemRequest(any(Long.class), any(ItemRequestDto.class))).thenReturn(itemRequestDtoResponse);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemRequestDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoResponse.getDescription())));
    }

    @Test
    void testGetItemRequestById() throws Exception {
        ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse();
        itemRequestDtoResponse.setId(2L);
        itemRequestDtoResponse.setDescription("itemRequestDtoDescription2");

        when(itemRequestService.getItemRequestById(any(Long.class))).thenReturn(itemRequestDtoResponse);

        mvc.perform(get("/requests/" + 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.description", is(itemRequestDtoResponse.getDescription())));
    }

    @Test
    void testGetItemRequests() throws Exception {
        List<ItemRequestDtoResponse> itemRequestDtoResponseList = new ArrayList<>();

        for (long i = 1; i < 5; i++) {
            ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse();
            itemRequestDtoResponse.setId(i);
            itemRequestDtoResponse.setDescription("itemRequestDtoDescription3");
            itemRequestDtoResponseList.add(itemRequestDtoResponse);
        }

        when(itemRequestService.getItemRequests()).thenReturn(itemRequestDtoResponseList);

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(itemRequestDtoResponseList.size())))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDtoResponseList.getFirst().getId().intValue())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDtoResponseList.getFirst().getDescription())));
    }

    @Test
    void testGetItemRequestsByUserId() throws Exception {
        List<ItemRequestDtoResponse> itemRequestDtoResponseList = new ArrayList<>();

        for (long i = 1; i < 5; i++) {
            ItemRequestDtoResponse itemRequestDtoResponse = new ItemRequestDtoResponse();
            itemRequestDtoResponse.setId(i);
            itemRequestDtoResponse.setDescription("itemRequestDtoDescription4");
            itemRequestDtoResponseList.add(itemRequestDtoResponse);
        }

        when(itemRequestService.getItemRequestsByUserId(any(Long.class))).thenReturn(itemRequestDtoResponseList);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(itemRequestDtoResponseList.size())))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDtoResponseList.getFirst().getId().intValue())))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDtoResponseList.getFirst().getDescription())));
    }
}
