package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.PlayerCreateRequest;

import java.util.List;

/**
 * @author : Xenofon Zinoviou
 */
public interface PlayerService {

    Player create(PlayerCreateRequest playerCreateRequest);

    Player addGameToPlayer(Integer playerId, Game game);

    Player getById(Integer id);

    List<Player> getAllPlayers();

}
