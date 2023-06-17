package com.xzinoviou.guessthenumber.exception;

import lombok.Getter;

/**
 * @author : Xenofon Zinoviou
 */
@Getter
public class GuessTheNumberException extends RuntimeException {

    public GuessTheNumberException(String message) {
        super(message);
    }
}
