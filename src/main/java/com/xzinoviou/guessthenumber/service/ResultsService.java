package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.results.GameResultsDto;
import com.xzinoviou.guessthenumber.dto.results.PlayerResultsDto;
import com.xzinoviou.guessthenumber.dto.results.TotalResultsDto;

/**
 * @author : Xenofon Zinoviou
 */
public interface ResultsService {

    GameResultsDto getGameResultsById(Integer id);

    PlayerResultsDto getPlayerResultsById(Integer playerId);

    TotalResultsDto getPlayersTotalResultsRanking();
}
