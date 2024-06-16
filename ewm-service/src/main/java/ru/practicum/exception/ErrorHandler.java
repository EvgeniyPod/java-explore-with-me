package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.DateFormatter;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice("ru.practicum")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidPathVariableException(final InvalidPathVariableException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Incorrectly made request.",
                "BAD_REQUEST",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "The required object was not found.",
                "NOT_FOUND",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Integrity constraint has been violated.",
                "CONFLICT",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Integrity constraint has been violated.",
                "CONFLICT",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleIncorrectParticipationRequest(final InvalidParticipationRequest e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Integrity constraint has been violated.",
                "CONFLICT",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidEventStateOrDate(final InvalidEventStateOrDate e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                "FORBIDDEN",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Incorrectly made request.",
                "BAD_REQUEST",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNumberFormatException(final NumberFormatException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Incorrectly made request.",
                "BAD_REQUEST",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException2(final javax.validation.ConstraintViolationException e) {
        log.error(e.getMessage());
        return new ApiError(Arrays.toString(e.getStackTrace()),
                e.getMessage(),
                "Incorrectly made request.",
                "BAD_REQUEST",
                DateFormatter.toString(LocalDateTime.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("Unexpected error has occurred");
        return new ApiError(Arrays.toString(e.getStackTrace()),
                "Unexpected error has occurred",
                e.getCause().toString(),
                "INTERNAL_SERVER_ERROR",
                DateFormatter.toString(LocalDateTime.now()));
    }
}