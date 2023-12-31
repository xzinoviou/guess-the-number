package com.xzinoviou.guessthenumber.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuessDto {

    private Integer score;

    private Integer attempt;
}
