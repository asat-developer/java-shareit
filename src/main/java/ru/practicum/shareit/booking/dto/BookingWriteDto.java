package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingWriteDto {

    @Null(groups = OnPatch.class, message = "Время начала бронирования нельзя изменить при обновлении !")
    @NotNull(groups = OnPost.class, message = "Отсутствует время начала бронирования предмета !")
    @FutureOrPresent(groups = {OnPost.class, OnPatch.class}, message = "Недопустимое время !")
    private LocalDateTime start;


    @Null(groups = OnPatch.class, message = "Время конца бронирования нельзя изменить при обновлении !")
    @NotNull(groups = OnPost.class, message = "Отсутствует время конца бронирования предмета !")
    @FutureOrPresent(groups = {OnPost.class, OnPatch.class}, message = "Недопустимое время !")
    private LocalDateTime end;


    @Null(groups = OnPatch.class, message = "Идентификатор предмета в заказе нельзя изменить при обновлении !")
    @NotNull(groups = OnPost.class, message = "Отсутствует идентификатор бронируемого предмета !")
    @Positive(groups = {OnPost.class, OnPatch.class}, message = "Идентификатор предмета должен быть больше 0 !")
    private Integer itemId;

    public interface OnPost {
    }

    public interface OnPatch {
    }
}
