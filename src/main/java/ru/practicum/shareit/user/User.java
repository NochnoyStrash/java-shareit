package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.generrat.Generated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Generated
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Имя должно быть задано")
    @Column(name = "name")
    private String name;
    @NotNull(message = "email должен быть задан")
    @Email(message = "Email не проходит валидацию")
    @Column(name = "email")
    private String email;

}
