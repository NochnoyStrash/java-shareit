package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.generrat.Generated;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Generated
public class ItemForRequest {
    private Long id;
    private String name;
    private Long ownerId;
}
