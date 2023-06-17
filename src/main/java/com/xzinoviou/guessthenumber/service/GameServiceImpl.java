package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dao.DatabaseDao;
import com.xzinoviou.guessthenumber.dto.GameResultsDto;
import com.xzinoviou.guessthenumber.dto.GameStatusInfoDto;
import com.xzinoviou.guessthenumber.exception.GuessTheNumberException;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.GameStatusInfo;
import com.xzinoviou.guessthenumber.model.Guess;
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

    private static final Integer DEFAULT_GAME_GUESS_ATTEMPTS = 3;
    private final DatabaseDao databaseDao;
    private final PlayerServiceImpl playerService;

    public GameServiceImpl(DatabaseDao databaseDao, PlayerServiceImpl playerService) {
        this.databaseDao = databaseDao;
        this.playerService = playerService;
    }

    @Override
    public GameStatusInfoDto create(GameCreateRequest gameCreateRequest) {
        try {
            Game game = Game.builder()
                    .id(databaseDao.getNextGameId())
                    .playerId(gameCreateRequest.getPlayerId())
                    .attempts(DEFAULT_GAME_GUESS_ATTEMPTS)
                    .guesses(new ArrayList<>())
                    .target(randomTargetGenerator())
                    .totalScore(0L)
                    .statusInfo(GameStatusInfo.CREATED)
                    .build();

            playerService.addGameToPlayer(gameCreateRequest.getPlayerId(), game);

            return GameStatusInfoDto.builder()
                    .status(game.getStatusInfo().getStatus())
                    .message(game.getStatusInfo().getMessage())
                    .build();
        } catch (RuntimeException ex) {
            throw new GuessTheNumberException("Fail to create game for player with id: " + gameCreateRequest.getPlayerId());
        }
    }

    @Override
    public GameStatusInfoDto update(GuessRequest guessRequest) {
        Game game = getGameById(guessRequest.getGameId());

        if (!game.getPlayerId().equals(guessRequest.getPlayerId())) {
            throw new GuessTheNumberException("Failed to update game due to invalid data provided");
        }

        if (game.getStatusInfo().isGameFinished()) {
            throw new GuessTheNumberException("Game is already finished");
        }

        try {

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
                game.setStatusInfo(GameStatusInfo.WON);
            } else if (game.getAttempts() == attempt) {
                game.setStatusInfo(GameStatusInfo.LOST);
            } else {
                game.setStatusInfo(GameStatusInfo.IN_PROGRESS);
            }

            game.setTotalScore(game.getTotalScore() + score);
            game.getGuesses().add(guess);

            return GameStatusInfoDto.builder()
                    .status(game.getStatusInfo().getStatus())
                    .message(game.getStatusInfo().getMessage())
                    .build();

        } catch (RuntimeException ex) {
            throw new GuessTheNumberException("Failed to update game for player with id: " + guessRequest.getPlayerId());
        }
    }

    @Override
    public GameResultsDto getGameResultsById(Integer id) {
        Game game = getGameById(id);

        return mapToGameResultsDto(game);
    }

    private Game getGameById(Integer id) {
        return databaseDao.getGames().stream().filter(game -> game.getId().equals(id)).findFirst()
                .orElseThrow(() -> new GuessTheNumberException("Failed to retrieve game with id: " + id));
    }

    private Integer randomTargetGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(100);
    }

    private GameResultsDto mapToGameResultsDto(Game game) {
        return GameResultsDto.builder()
                .gameId(game.getId())
                .playerId(game.getPlayerId())
                .attempts(game.getGuesses().size() + 1)
                .totalScore(game.getTotalScore())
                .statusInfo(
                        GameStatusInfoDto.builder()
                                .status(game.getStatusInfo().getStatus())
                                .message(game.getStatusInfo().getMessage())
                                .build()
                )
                .build();
    }
}
