package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Creating;
import ru.practicum.shareit.Updating;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя должно быть задано", groups = Creating.class)
    private String name;
    @NotNull(message = "email должен быть задан", groups = Creating.class)
    @Email(message = "Email не проходит валидацию", groups = {Creating.class, Updating.class})
    private String email;

}
