package com.xzinoviou.guessthenumber.dto.results;

import com.xzinoviou.guessthenumber.dto.game.GameStatusInfoDto;
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

    private Integer attempts;

    private GameStatusInfoDto statusInfo;
}
