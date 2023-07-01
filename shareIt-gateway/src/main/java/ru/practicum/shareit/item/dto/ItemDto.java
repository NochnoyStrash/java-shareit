package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.Creating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Generated
public class ItemDto {
    private long id;
    @NotBlank(message = "Название не может быть пустым", groups = Creating.class)
    private String name;
    @NotBlank(message = "Описание не может быть пустым", groups = Creating.class)
    private String description;
    @NotNull(groups = {Creating.class})
    private Boolean available;
    private Long requestId;


}
