package com.maitrack.maitrackapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MTAuthException extends RuntimeException{

    public MTAuthException(String message) {
        super(message);
    }

}
