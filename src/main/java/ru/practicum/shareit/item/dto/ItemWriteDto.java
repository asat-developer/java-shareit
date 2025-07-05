package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemWriteDto {

    @NotBlank(message = "Отсутствует наименование предмета !")
    private String name;

    @NotBlank(message = "Отсутствует описание предмета !")
    private String description;

    @NotNull(message = "Отсутствует статус возможности забранировать предмет !")
    private Boolean available;

    private Integer ownerId;

    private Integer requestId;
}
