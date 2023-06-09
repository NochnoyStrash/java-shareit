package ru.practicum.shareit.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.ValidateBookingAndItemxception;
import ru.practicum.shareit.booking.exception.ValidateBookingException;
import ru.practicum.shareit.generrat.Generated;
import ru.practicum.shareit.item.exception.CommentsValidateException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.request.exception.NotFoundRequestException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
@Generated
public class ErrorHandler {

    @ExceptionHandler({ UserNotFoundException.class, ItemNotFoundException.class,
            BookingNotFoundException.class, ValidateBookingAndItemxception.class, NotFoundRequestException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFound(RuntimeException e) {
        return  new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectUserData(ConstraintViolationException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ValidateBookingException.class, CommentsValidateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectUserData(RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExceptions(Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

}
