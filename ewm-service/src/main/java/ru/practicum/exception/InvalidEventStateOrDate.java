package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidEventStateOrDate extends RuntimeException {
    public InvalidEventStateOrDate(String message) {
        super(message);
    }
}