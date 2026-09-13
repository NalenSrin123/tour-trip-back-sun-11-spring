package com.etec.tourtripapi.schedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScheduleValidationException extends RuntimeException {

    public ScheduleValidationException(String message) {
        super(message);
    }
}
