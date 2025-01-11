package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.enums.SearchType;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    void testCreateBooking() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("UserName1");
        user1.setEmail("userEmail1@user.com");

        Item item = new Item();
        item.setId(1L);
        item.setName("ItemName1");
        item.setDescription("ItemDescription1");
        item.setAvailable(true);
        item.setOwner(user1);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(1L);
        bookingDto.setBooker(user1);
        bookingDto.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setItem(item);
        bookingDtoResponse.setBooker(user1);
        bookingDtoResponse.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);

        when(bookingService.createBooking(any(Long.class), any(BookingDto.class))).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    void testUpdateBooking() throws Exception {
        User user1 = new User();
        user1.setId(2L);
        user1.setName("UserName2");
        user1.setEmail("userEmail2@user.com");

        Item item = new Item();
        item.setId(2L);
        item.setName("ItemName2");
        item.setDescription("ItemDescription2");
        item.setAvailable(true);
        item.setOwner(user1);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(2L);
        bookingDto.setItemId(2L);
        bookingDto.setBooker(user1);
        bookingDto.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(2L);
        bookingDtoResponse.setItem(item);
        bookingDtoResponse.setBooker(user1);
        bookingDtoResponse.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);

        when(bookingService.updateBooking(any(Long.class), any(Long.class), any(Boolean.class))).thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/" + 2L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().name())));
    }

    @Test
    void testGetBookingById() throws Exception {
        User user1 = new User();
        user1.setId(3L);
        user1.setName("UserName3");
        user1.setEmail("userEmail3@user.com");

        Item item = new Item();
        item.setId(3L);
        item.setName("ItemName3");
        item.setDescription("ItemDescription3");
        item.setAvailable(true);
        item.setOwner(user1);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(3L);
        bookingDto.setItemId(3L);
        bookingDto.setBooker(user1);
        bookingDto.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDto.setStatus(BookingStatus.WAITING);

        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(3L);
        bookingDtoResponse.setItem(item);
        bookingDtoResponse.setBooker(user1);
        bookingDtoResponse.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);

        when(bookingService.getBookingById(any(Long.class), any(Long.class))).thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/" + 3L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 3L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId().intValue())))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().truncatedTo(ChronoUnit.SECONDS).toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    void testGetBookingsByUserIdAndStatus() throws Exception {
        User user1 = new User();
        user1.setId(4L);
        user1.setName("UserName4");
        user1.setEmail("userEmail4@user.com");

        Item item = new Item();
        item.setId(4L);
        item.setName("ItemName4");
        item.setDescription("ItemDescription4");
        item.setAvailable(true);
        item.setOwner(user1);

        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();

        for (long i = 1; i < 5; i++) {
            BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
            bookingDtoResponse.setId(i);
            bookingDtoResponse.setItem(item);
            bookingDtoResponse.setBooker(user1);
            bookingDtoResponse.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
            bookingDtoResponse.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
            bookingDtoResponse.setStatus(BookingStatus.APPROVED);
            bookingDtoResponseList.add(bookingDtoResponse);
        }

        when(bookingService.getBookingsByUserIdAndStatus(any(Long.class), any(SearchType.class))).thenReturn(bookingDtoResponseList);

        mvc.perform(get("/bookings")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(bookingDtoResponseList.size())))
                .andExpect(jsonPath("$.[0].id", is(bookingDtoResponseList.getFirst().getId().intValue())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDtoResponseList.getFirst().getBooker().getId().intValue())));
    }

    @Test
    void testGetBookingsByUserItemsAndStatus() throws Exception {
        User user1 = new User();
        user1.setId(5L);
        user1.setName("UserName5");
        user1.setEmail("userEmail5@user.com");

        Item item = new Item();
        item.setId(5L);
        item.setName("ItemName5");
        item.setDescription("ItemDescription5");
        item.setAvailable(true);
        item.setOwner(user1);

        List<BookingDtoResponse> bookingDtoResponseList = new ArrayList<>();

        for (long i = 1; i < 5; i++) {
            BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
            bookingDtoResponse.setId(i);
            bookingDtoResponse.setItem(item);
            bookingDtoResponse.setBooker(user1);
            bookingDtoResponse.setStart(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS));
            bookingDtoResponse.setEnd(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS));
            bookingDtoResponse.setStatus(BookingStatus.APPROVED);
            bookingDtoResponseList.add(bookingDtoResponse);
        }

        when(bookingService.getBookingsByUserItemsAndStatus(any(Long.class), any(SearchType.class))).thenReturn(bookingDtoResponseList);

        mvc.perform(get("/bookings/owner")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(bookingDtoResponseList.size())))
                .andExpect(jsonPath("$.[0].id", is(bookingDtoResponseList.getFirst().getId().intValue())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDtoResponseList.getFirst().getBooker().getId().intValue())));
    }
}
