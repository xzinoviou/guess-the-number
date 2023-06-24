package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.GameStatus;
import com.xzinoviou.guessthenumber.model.Guess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author : Xenofon Zinoviou
 */
@ExtendWith(MockitoExtension.class)
class GameServiceImplTest {
    private static final Integer TARGET = 44;
    private static final Integer GAME_ID = 1;
    private static final Integer PLAYER_ID = 1;
    private DatabaseDao databaseDao;
    private PlayerServiceImpl playerService;
    private GameServiceImpl testClass;


    @BeforeEach
    void init() {
        databaseDao = Mockito.mock(DatabaseDao.class);
        playerService = Mockito.mock(PlayerServiceImpl.class);
        testClass = new GameServiceImpl(databaseDao, playerService);
    }

    @AfterEach
    void destroy() {
        testClass = null;
        databaseDao = null;
        playerService = null;
    }

    @Test
    void getById_whenSupplyExistingGameId_thenReturnGame() {
        when(databaseDao.getGames()).thenReturn(listOfGames());

        Game result = testClass.getById(GAME_ID);

        assertEquals(GAME_ID.intValue(), result.getId().intValue());

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
