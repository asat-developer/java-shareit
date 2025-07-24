package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingWriteDto {

    @NotNull(message = "Отсутствует время начала бронирования предмета !")
    private LocalDateTime start;

    @NotNull(message = "Отсутствует время конца бронирования предмета !")
    private LocalDateTime end;

    @NotNull(message = "Отсутствует идентификатор бронируемого предмета !")
    @Positive(message = "Идентификатор предмета должен быть больше 0 !")
    private Integer itemId;
}
