package ru.practicum.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("ru.practicum.stats")
public class ErrorHandler {

    @ExceptionHandler({InvalidPathVariableException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception e) {
        return handleException(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerError(final Throwable e) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ErrorResponse handleException(HttpStatus status, Throwable e) {
        log.error("An error occurred: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), status.value());
    }
}
