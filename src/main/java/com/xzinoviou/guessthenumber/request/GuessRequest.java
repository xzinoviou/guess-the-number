package com.xzinoviou.guessthenumber.request;

import lombok.Getter;

/**
 * @author : Xenofon Zinoviou
 */
@Getter
public class GuessRequest {

    private Integer gameId;

    private Integer playerId;

    private Integer guessTarget;
}
