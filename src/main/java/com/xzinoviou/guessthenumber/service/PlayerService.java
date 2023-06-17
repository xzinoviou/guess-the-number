package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.PlayerCreateRequest;

/**
 * @author : Xenofon Zinoviou
 */
public interface PlayerService {

    Player create(PlayerCreateRequest playerCreateRequest);

    Player addGameToPlayer(Integer playerId, Game game);

    Player getById(Integer id);

}
