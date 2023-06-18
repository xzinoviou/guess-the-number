package com.xzinoviou.guessthenumber.service;

import com.xzinoviou.guessthenumber.dto.game.GameStatusDto;
import com.xzinoviou.guessthenumber.dto.results.GameResultsDto;
import com.xzinoviou.guessthenumber.dto.results.PlayerResultsDto;
import com.xzinoviou.guessthenumber.dto.results.TotalResultsDto;
import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.model.GameStatus;
import com.xzinoviou.guessthenumber.model.Player;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author : Xenofon Zinoviou
 */
@Service
public class ResultsServiceImpl implements ResultsService {

    private final PlayerService playerService;
    private final GameService gameService;

    public ResultsServiceImpl(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    @Override
    public GameResultsDto getGameResultsByGameId(Integer id) {
        Game game = gameService.getById(id);

        return mapToGameResultsDto(game);
    }

    @Override
    public PlayerResultsDto getPlayerResultsByPlayerId(Integer playerId) {
        Player player = playerService.getById(playerId);

        return mapToPlayerResultsDto(player);
    }

    @Override
    public TotalResultsDto getPlayersTotalResultsRanking() {
        List<PlayerResultsDto> playerResultsDtoList = playerService.getAllPlayers().stream().map(this::mapToPlayerResultsDto)
                .sorted(Comparator.comparing(PlayerResultsDto::getWon).reversed()).toList();

        return TotalResultsDto.builder()
                .playersResultsRanking(playerResultsDtoList)
                .build();
    }

    private GameResultsDto mapToGameResultsDto(Game game) {
        return GameResultsDto.builder()
                .gameId(game.getId())
                .playerId(game.getPlayerId())
                .attempts(game.getGuesses().size())
                .totalScore(game.getTotalScore())
                .statusInfo(
                        GameStatusDto.builder()
                                .status(game.getStatusInfo().getStatus())
                                .message(game.getStatusInfo().getMessage())
                                .build()
                )
                .build();
    }

    private PlayerResultsDto mapToPlayerResultsDto(Player player) {
        Integer won = Math.toIntExact(player.getHistory().stream().filter(game -> game.getStatusInfo() == GameStatus.WON).count());
        Integer lost = Math.toIntExact(player.getHistory().stream().filter(game -> game.getStatusInfo() == GameStatus.LOST).count());
        BigDecimal avgWinRatio = BigDecimal.valueOf(won / (double) player.getTotalGames());

        long totalScore = player.getHistory().stream().map(Game::getTotalScore).mapToLong(Long::longValue).sum();

        return PlayerResultsDto.builder()
                .id(player.getId())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .dateOfBirth(player.getDateOfBirth())
                .totalGames(player.getTotalGames())
                .totalScore(totalScore)
                .won(won)
                .lost(lost)
                .avgWinRatio(avgWinRatio)
                .build();
    }
}
