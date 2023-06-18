package com.xzinoviou.guessthenumber.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author : Xenofon Zinoviou
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Game {

    private Integer id;
    private Integer playerId;
    private Integer target;
    private Long totalScore;
    private GameStatus statusInfo;
    private Integer attempts;
    private List<Guess> guesses;
}
