package com.xzinoviou.guessthenumber.model;

/**
 * @author : Xenofon Zinoviou
 */
public enum GameStatusInfo {
    CREATED("Created", "Make a guess"),
    IN_PROGRESS("In progress", "Wrong Guess. Please try again"),
    WON("Won", "Success, you win!!"),
    LOST("Lost", "Game over.");

    private final String status;
    private final String message;

    GameStatusInfo(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGameFinished() {
        return this == WON || this == LOST;
    }
}
