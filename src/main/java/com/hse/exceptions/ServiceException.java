package com.hse.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceException extends ResponseStatusException {
    public ServiceException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
