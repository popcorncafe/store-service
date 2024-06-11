package com.popcorncafe.storeservice.controller;

import com.popcorncafe.storeservice.StoreServiceApplication;
import com.popcorncafe.storeservice.exsception.ResourceNotFoundException;
import com.popcorncafe.storeservice.service.dto.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerAdvice {

    private final Logger log = LoggerFactory.getLogger(StoreServiceApplication.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public MessageResponse resourceNotFoundHandler(ResourceNotFoundException e) {

        log.warn("Resource not found, message: {} ", e.getMessage());
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
