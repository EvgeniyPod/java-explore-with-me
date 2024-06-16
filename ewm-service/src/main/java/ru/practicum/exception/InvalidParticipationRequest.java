package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidParticipationRequest extends RuntimeException {
    public InvalidParticipationRequest(String message) {
        super(message);
    }
}