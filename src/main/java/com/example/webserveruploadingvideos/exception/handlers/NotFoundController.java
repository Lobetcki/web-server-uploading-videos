package com.example.webserveruploadingvideos.exception.handlers;

import com.example.webserveruploadingvideos.exception.InvalidParametersExeption;
import com.example.webserveruploadingvideos.exception.ItNotFoundException;
import com.example.webserveruploadingvideos.exception.UnauthorizedExeption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class NotFoundController {
    @ExceptionHandler(ItNotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).body("Not Found");
    }

    @ExceptionHandler(InvalidParametersExeption.class)
    public ResponseEntity<?> invalidParam() {
        return ResponseEntity.status(400).body("Error download");
    }

    @ExceptionHandler(UnauthorizedExeption.class)
    public ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(401).body("Unauthorized");
    }
}
