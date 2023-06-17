package com.xzinoviou.guessthenumber.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author : Xenofon Zinoviou
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Guess {

    private Integer gameId;

    private Integer playerId;

    private Integer score;

    private Integer attempt;

    private Integer guessTarget;

}
