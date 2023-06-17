package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.GameStatus;
import com.xzinoviou.guessthenumber.model.Guess;
import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * @author : Xenofon Zinoviou
 */
@Component
public class GameServiceImpl implements GameService {

    private final DatabaseDao databaseDao;
    private final PlayerServiceImpl playerService;

    public GameServiceImpl(DatabaseDao databaseDao, PlayerServiceImpl playerService) {
        this.databaseDao = databaseDao;
        this.playerService = playerService;
    }

    @Override
    public Game create(GameCreateRequest gameCreateRequest) {
        try {
            Game game = Game.builder()
                    .id(databaseDao.getNextGameId())
                    .playerId(gameCreateRequest.getPlayerId())
                    .attempts(gameCreateRequest.getNumberOfAttempts())
                    .guesses(new ArrayList<>())
                    .target(randomTargetGenerator())
                    .totalScore(0L)
                    .status(GameStatus.NOT_STARTED)
                    .build();

            playerService.addGameToPlayer(gameCreateRequest.getPlayerId(), game);

            return game;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Fail to create game for player with id: " + gameCreateRequest.getPlayerId());
        }
    }

    @Override
    public Game update(GuessRequest guessRequest) {
        try {
            Game game = getGameByIdAndPlayerId(guessRequest.getPlayerId(), guessRequest.getGameId());

            Guess guess = Guess.builder()
                    .gameId(game.getId())
                    .playerId(game.getPlayerId())
                    .guessTarget(guessRequest.getGuessTarget())
                    .build();

            // If score matches -> 10 points , else 1 point
            int score = game.getTarget().equals(guessRequest.getGuessTarget()) ? 10 : 1;
            guess.setScore(score);

            //find previous attempt number + increase by 1
            int attempt = game.getGuesses().size() + 1;
            guess.setAttempt(attempt);

            //set game status

            if(score == 10){
                game.setStatus(GameStatus.SUCCESS);
            } else if(game.getAttempts() == attempt) {
                game.setStatus(GameStatus.BETTER_LUCK_NEXT_TIME);
            } else {
                game.setStatus(GameStatus.TRY_AGAIN);
            }

            game.getGuesses().add(guess);

            return game;

        } catch (RuntimeException ex) {
            throw new RuntimeException("Fail to update game for player with id: " + guessRequest.getPlayerId());
        }
    }

    private Integer randomTargetGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(100);
    }

    public Game getGameByIdAndPlayerId(Integer playerId, Integer gameId) {
        try {
            Player player = playerService.getById(playerId);

            return player.getHistory().stream().filter(game -> game.getId().equals(gameId)).findFirst()
                    .orElseThrow(() -> new RuntimeException("Fail to retrieve game with id: " + gameId));

        } catch (RuntimeException ex) {
            throw new RuntimeException("Fail to retrieve game with id: " + gameId + " from player with id: " + playerId);
        }
    }
}
