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
public class GameResults {

    private Integer gameId;

    private String playerName;

    private Long totalScore;

    private GameStatus status;

    private GameMessage message;

    private Integer attempts;
}
