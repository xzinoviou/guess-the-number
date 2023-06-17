package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface GameService {

    Game create(GameCreateRequest gameCreateRequest);

    Game update(GuessRequest guessRequest);
}
