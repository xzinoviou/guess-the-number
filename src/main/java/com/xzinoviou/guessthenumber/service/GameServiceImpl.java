package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.exception.GuessTheNumberException;
import com.xzinoviou.guessthenumber.model.*;
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

    private final static Integer DEFAULT_GAME_GUESS_ATTEMPTS = 3;
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
                    .attempts(DEFAULT_GAME_GUESS_ATTEMPTS)
                    .guesses(new ArrayList<>())
                    .target(randomTargetGenerator())
                    .totalScore(0L)
                    .status(GameStatus.CREATED)
                    .message(GameMessage.MAKE_A_GUESS)
                    .build();

            playerService.addGameToPlayer(gameCreateRequest.getPlayerId(), game);

            return game;
        } catch (RuntimeException ex) {
            throw new GuessTheNumberException("Fail to create game for player with id: " + gameCreateRequest.getPlayerId());
        }
    }

    @Override
    public Game update(GuessRequest guessRequest) {
        try {
            Game game = getGameByIdAndPlayerId(guessRequest.getPlayerId(), guessRequest.getGameId());

            if (game.getStatus() == GameStatus.FINISHED) {
                throw new GuessTheNumberException("Game is already finished");
            }

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

            if (score == 10) {
                game.setMessage(GameMessage.SUCCESS);
                game.setStatus(GameStatus.FINISHED);
            } else if (game.getAttempts() == attempt) {
                game.setMessage(GameMessage.GAME_OVER);
                game.setStatus(GameStatus.FINISHED);
            } else {
                game.setMessage(GameMessage.WRONG_GUESS_PLEASE_TRY_AGAIN);
                game.setStatus(GameStatus.IN_PROGRESS);
            }

            game.getGuesses().add(guess);

            return game;

        } catch (RuntimeException ex) {
            throw new GuessTheNumberException("Fail to update game for player with id: " + guessRequest.getPlayerId());
        }
    }

    public Game getGameByIdAndPlayerId(Integer playerId, Integer gameId) {
        Player player = playerService.getById(playerId);

        return player.getHistory().stream().filter(game -> game.getId().equals(gameId)).findFirst()
                .orElseThrow(() -> new GuessTheNumberException("Fail to retrieve game with id: " + gameId));
    }

    @Override
    public Game getGameResultsById(Integer id) {
        return null;
    }

    private Integer randomTargetGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(100);
    }


}
