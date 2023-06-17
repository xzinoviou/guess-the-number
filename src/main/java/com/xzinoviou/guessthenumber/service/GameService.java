package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.GameStatusInfoDto;
import com.xzinoviou.guessthenumber.dto.GameResultsDto;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface GameService {

    GameStatusInfoDto create(GameCreateRequest gameCreateRequest);

    GameStatusInfoDto update(GuessRequest guessRequest);

    GameResultsDto getGameResultsById(Integer id);
}
