package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.game.GameStatusDto;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface GameService {

    GameStatusDto create(GameCreateRequest gameCreateRequest);

    GameStatusDto guess(GuessRequest guessRequest);

    Game getById(Integer id);
}
