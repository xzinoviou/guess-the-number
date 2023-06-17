package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.PlayerCreateRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author : Xenofon Zinoviou
 */
@Component
public class PlayerServiceImpl implements PlayerService{

    private final DatabaseDao databaseDao;

    public PlayerServiceImpl(DatabaseDao databaseDao) {
        this.databaseDao = databaseDao;
    }

    @Override
    public Player create(PlayerCreateRequest playerCreateRequest) {
        try {
            Player player = Player.builder()
                    .id(databaseDao.getNextPlayerId())
                    .firstName(playerCreateRequest.getFirstName())
                    .lastName(playerCreateRequest.getLastName())
                    .dateOfBirth(playerCreateRequest.getDateOfBirth())
                    .totalGames(0)
                    .history(new ArrayList<>())
                    .build();

            return databaseDao.persistPlayer(player);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Failed to create player");
        }
    }

    @Override
    public Player addGameToPlayer(Integer playerId, Game game) {
        try {
            Player player = getById(playerId);
            player.getHistory().add(game);

            return player;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Fail to add new game to player with id: " + playerId);
        }
    }

    @Override
    public Player getById(Integer id) {
        return databaseDao.getPlayers().stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Fail to retrieve player with id: " + id));
    }
}
