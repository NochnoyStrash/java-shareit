package ru.practicum.shareit.user;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    @NotNull(message = "Имя должно быть задано")
    private String name;
    @NotNull(message = "email должен быть задан")
    @Email(message = "Email не проходит валидацию")
    private String email;
}
