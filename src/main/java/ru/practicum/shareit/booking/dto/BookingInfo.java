package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.generrat.Generated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class BookingInfo {
    private Long id;
    private Long bookerId;
}
