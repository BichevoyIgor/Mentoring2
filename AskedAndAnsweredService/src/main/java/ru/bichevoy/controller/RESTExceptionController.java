package ru.bichevoy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bichevoy.exception.NotFoundException;

@RestControllerAdvice
public class RESTExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> idNotFound(NotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
