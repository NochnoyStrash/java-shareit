package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	private static final String userIdHeaders = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(userIdHeaders) long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader(userIdHeaders) long userId,
			@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(userIdHeaders) long userId,
			@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> confirmBooking(@RequestHeader(userIdHeaders) long userId,
												 @PathVariable Long bookingId,
												 @RequestParam boolean approved) {
		return bookingClient.confirmBooking(userId, bookingId, approved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllByOwner(@RequestHeader(userIdHeaders) long userId, @RequestParam(defaultValue = "ALL") String state,
												@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
												@RequestParam(defaultValue = "20") @PositiveOrZero Integer size) {
		BookingState state1 = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		return bookingClient.getAllByOwner(userId, state1, from, size);
	}

}