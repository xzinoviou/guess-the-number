package com.xzinoviou.guessthenumber.dto.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlayerResultsDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private Integer totalGames;

    private Long totalScore;

    private Integer won;

    private Integer lost;

    private BigDecimal avgWinRatio;
}
