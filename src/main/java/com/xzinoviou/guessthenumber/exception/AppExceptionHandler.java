package com.xzinoviou.guessthenumber.exception;

import com.xzinoviou.guessthenumber.AppError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * @author : Xenofon Zinoviou
 */
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(GuessTheNumberException.class)
    public AppError handleAppException(final GuessTheNumberException ex) {
        return AppError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .message(ex.getMessage())
                .build();
    }
}
