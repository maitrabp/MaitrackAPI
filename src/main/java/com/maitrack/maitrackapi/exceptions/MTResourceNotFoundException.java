package com.maitrack.maitrackapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MTResourceNotFoundException extends RuntimeException{
    MTResourceNotFoundException(String message) {
        super(message);
    }
}
