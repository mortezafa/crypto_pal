package org.project.cryptopal.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicateUsers (DataIntegrityViolationException e) {
       String errorMessage = "A user with the provided email or username already exists.";
       return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
   }


}
