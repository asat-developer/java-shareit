package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingWriteDto;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public BookingClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> saveBooking(long userId, BookingWriteDto bookingWriteDto) {
        return post("", userId, bookingWriteDto);
    }

    public ResponseEntity<Object> updateBooking(long userId, long bookingId, Boolean approvedStatus) {
        Map<String, Object> parameters = Map.of("approved", approvedStatus);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }


    public ResponseEntity<Object> getBookingByBookingId(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsByUser(long userId, State state) {
        Map<String, Object> parameters = Map.of("state", state);
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(long userId, State state) {
        Map<String, Object> parameters = Map.of("state", state);
        return get("/owner?state={state}", userId);
    }


}
