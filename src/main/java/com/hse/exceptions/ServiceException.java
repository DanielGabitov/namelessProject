package com.hse.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


//todo разобраться с Exception'ами и кодами возврата
public class ServiceException extends ResponseStatusException {

    public ServiceException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
