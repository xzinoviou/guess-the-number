package com.xzinoviou.guessthenumber.dao;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Xenofon Zinoviou
 */
@Component
public class DatabaseDao {

    private static final List<Player> PLAYERS = new ArrayList<>();

    private static int PLAYER_ID_SEQUENCER;
    private static int GAME_ID_SEQUENCER;

    public List<Player> getPlayers() {
        return PLAYERS != null ? PLAYERS : new ArrayList<>();
    }

    public List<Game> getGames() {
        return getPlayers().stream().map(Player::getHistory).flatMap(Collection::stream).toList();
    }

    public Player persistPlayer(Player player) {
        PLAYERS.add(player);
        return player;
    }

    public int getNextPlayerId() {
        return ++PLAYER_ID_SEQUENCER;
    }

    public int getNextGameId() {
        return ++GAME_ID_SEQUENCER;
    }
}
