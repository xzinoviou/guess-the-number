package com.xzinoviou.guessthenumber.request;

import lombok.Getter;

/**
 * @author : Xenofon Zinoviou
 */
@Getter
public class GameCreateRequest {

    private Integer playerId;
    private Integer numberOfAttempts;
}
