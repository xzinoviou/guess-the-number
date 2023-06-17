package com.xzinoviou.guessthenumber.exception;

import com.xzinoviou.guessthenumber.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

/**
 * @author : Xenofon Zinoviou
 */
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GuessTheNumberException.class)
    public ResponseEntity<AppError> handleAppException(final GuessTheNumberException ex) {
        return new ResponseEntity<>(AppError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .message(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
