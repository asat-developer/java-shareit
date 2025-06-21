package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWriteDto {

    @Email(groups = {OnPatch.class, OnPost.class}, message = "Электронная почта должна соответствовать определённому формату !")
    @NotBlank(groups = OnPost.class, message = "Отсутствует электронная почта !")
    private String email;

    @NotBlank(groups = OnPost.class, message = "Отсутствует имя пользователя !")
    private String name;

    public interface OnPost {
    }

    public interface OnPatch {
    }

}
