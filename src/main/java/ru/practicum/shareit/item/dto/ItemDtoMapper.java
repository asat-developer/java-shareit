package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingReadDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemDtoMapper {
    public static Item itemWriteDtoToItem(ItemWriteDto itemWriteDto, User user, ItemRequest request) {
        Item item = new Item();
        item.setName(itemWriteDto.getName());
        item.setDescription(itemWriteDto.getDescription());
        item.setAvailable(itemWriteDto.getAvailable());
        item.setOwner(user);
        item.setRequest(request);
        return item;
    }

    public static ItemReadDto itemToItemReadDto(Item item) {
        ItemRequest request = item.getRequest();
        Integer requestId;
        if (request == null) {
            requestId = null;
        } else {
            requestId = request.getId();
        }
        return new ItemReadDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                requestId);
    }

    public static Comment commentWriteDtoToComment(CommentWriteDto commentWriteDto, User author, Item item) {
        Comment comment = new Comment();
        comment.setText(commentWriteDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static CommentReadDto commentToCommentReadDto(Comment comment) {
        return new CommentReadDto(comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static ItemReadDtoWithBookingsAndComments itemToItemReadDtoWithBookingsAndComments(Item item,
                                                                                        List<CommentReadDto> comments,
                                                                                        BookingReadDto lastBooking,
                                                                                        BookingReadDto nextBooking) {
        ItemRequest request = item.getRequest();
        Integer requestId;
        if (request == null) {
            requestId = null;
        } else {
            requestId = request.getId();
        }
        return new ItemReadDtoWithBookingsAndComments(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                requestId,
                comments,
                lastBooking,
                nextBooking);
    }
}
