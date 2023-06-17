package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.GameResultsDto;
import com.xzinoviou.guessthenumber.model.GameMessage;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface GameService {

    GameMessage create(GameCreateRequest gameCreateRequest);

    GameMessage update(GuessRequest guessRequest);

    GameResultsDto getGameResultsById(Integer id);
}
