package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.game.GameStatusInfoDto;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface GameService {

    GameStatusInfoDto create(GameCreateRequest gameCreateRequest);

    GameStatusInfoDto update(GuessRequest guessRequest);

    Game getById(Integer id);
}
