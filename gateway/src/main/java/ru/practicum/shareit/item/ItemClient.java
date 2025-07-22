package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentWriteDto;
import ru.practicum.shareit.item.dto.ItemWriteDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }


    public ResponseEntity<Object> getAllItems() {
        return get("/all");
    }

    public ResponseEntity<Object> saveItem(int userId, ItemWriteDto itemWriteDto) {
        return post("", userId, itemWriteDto);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemWriteDto itemWriteDto) {
        return patch("/" + itemId, userId, itemWriteDto);
    }

    public ResponseEntity<Object> getAllItemsByUser(long userId) {
        return get("");
    }

    public ResponseEntity<Object> searchByText(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", parameters);
    }

    public ResponseEntity<Object> saveComment(long userId, long itemId, CommentWriteDto commentWriteDto) {
        return post("/" + itemId + "/comment", userId, commentWriteDto);
    }
}
