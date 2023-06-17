package com.xzinoviou.guessthenumber.dto;

import com.xzinoviou.guessthenumber.model.GameMessage;
import com.xzinoviou.guessthenumber.model.GameStatus;
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
public class GameResultsDto {

    private Integer gameId;

    private Integer playerId;

    private Long totalScore;

    private GameStatus status;

    private GameMessage message;

    private Integer attempts;
}
