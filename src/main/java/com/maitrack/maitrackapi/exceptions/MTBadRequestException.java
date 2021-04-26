package com.maitrack.maitrackapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MTBadRequestException extends RuntimeException{

    public MTBadRequestException(String message) {
        super(message);
    }
}
