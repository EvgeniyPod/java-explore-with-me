package ru.practicum.stats.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPathVariableException extends RuntimeException {
    public InvalidPathVariableException(String message) {
        super(message);
    }
}