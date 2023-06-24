package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.exception.GuessTheNumberException;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.GameStatus;
import com.xzinoviou.guessthenumber.model.Guess;
import com.xzinoviou.guessthenumber.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * @author : Xenofon Zinoviou
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    private static final Integer TARGET = 44;
    private static final Integer GAME_ID = 1;
    private static final Integer PLAYER_ID = 1;
    private DatabaseDao databaseDaoMock;
    private PlayerServiceImpl testClass;


    @BeforeEach
    void init() {
        databaseDaoMock = Mockito.mock(DatabaseDao.class);
        testClass = new PlayerServiceImpl(databaseDaoMock);
    }

    @Test
    void getById_whenSupplyExistingPlayerId_thenReturnPlayer() {
        when(databaseDaoMock.getPlayers()).thenReturn(listOfPlayers());

        Player result = testClass.getById(PLAYER_ID);

        assertEquals(PLAYER_ID, result.getId());
        assertEquals(1, result.getHistory().size());
    }

    @Test
    void getById_whenSupplyInvalidPlayerId_thenThrowGuessTheNumberException() {
        Integer playerId = 44;
        when(databaseDaoMock.getPlayers()).thenReturn(listOfPlayers());

        GuessTheNumberException ex = assertThrows(GuessTheNumberException.class, () -> testClass.getById(playerId));

        assertEquals(String.format("Failed to retrieve player with id: %d", playerId), ex.getMessage());
    }

    private List<Player> listOfPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(Player.builder()
                .id(1)
                .history(listOfGames())
                .firstName("player_firstname")
                .lastName("player_lastname")
                .totalGames(1)
                .build());
        return players;
    }

    private List<Game> listOfGames() {
        List<Game> games = new ArrayList<>();
        games.add(Game.builder()
                .id(GAME_ID)
                .playerId(PLAYER_ID)
                .guesses(singleGuessList())
                .totalScore(1L)
                .target(TARGET)
                .statusInfo(GameStatus.IN_PROGRESS)
                .build());
        return games;
    }

    private List<Guess> singleGuessList() {
        List<Guess> guesses = new ArrayList<>();
        guesses.add(Guess.builder()
                .playerId(1)
                .gameId(1)
                .attempt(1)
                .guessTarget(33)
                .score(1)
                .build());
        return guesses;
    }
}
