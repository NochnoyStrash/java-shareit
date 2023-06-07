package ru.practicum.shareit.booking.exception;

public class ValidateBookingException extends RuntimeException {
    public ValidateBookingException(String massage) {
        super(massage);
    }
}
