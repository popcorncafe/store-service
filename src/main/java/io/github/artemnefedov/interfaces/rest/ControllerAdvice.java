package io.github.artemnefedov.interfaces.rest;

import io.github.artemnefedov.entity.MessageResponse;
import io.github.artemnefedov.exsception.ResourceNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(NOT_FOUND)
    public MessageResponse resourceNotFoundException(ResourceNotFound e) {
        return new MessageResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public MessageResponse handleException(Exception e) {

        log.error(e.getMessage());
        e.printStackTrace();
        return new MessageResponse("Internal error");
    }
}
