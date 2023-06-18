package com.xzinoviou.guessthenumber.dto.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TotalResultsDto {
    private List<PlayerResultsDto> playersResultsRanking;
}
