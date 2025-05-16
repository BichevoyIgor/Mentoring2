package ru.bichevoy.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bichevoy.exception.NotFoundException;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String idNotFound(NotFoundException exception, Model model) {
        model.addAttribute("message", exception);
        return "error";
    }
}
