package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.dto.MessageResponse;
import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public MessageResponse resourceNotFoundHandler(ResourceNotFoundException e) {
        return new MessageResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public MessageResponse internalErrorHandler(Exception ex) {

        log.error("Internal error, message: {} ", ex.getMessage());
        ex.printStackTrace();
        return new MessageResponse("Internal error.");
    }
}
