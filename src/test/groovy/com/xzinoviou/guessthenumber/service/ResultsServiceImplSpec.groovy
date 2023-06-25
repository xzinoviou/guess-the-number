package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.model.Game
import com.xzinoviou.guessthenumber.model.GameStatus
import com.xzinoviou.guessthenumber.model.Guess
import com.xzinoviou.guessthenumber.model.Player
import spock.lang.Specification

import java.time.Instant

/**
 * @author : Xenofon Zinoviou
 */
class ResultsServiceImplSpec extends Specification {

    private PlayerService playerService

    private GameService gameService

    private ResultsServiceImpl testClass

    void setup() {
        playerService = Mock(PlayerServiceImpl)
        gameService = Mock(GameServiceImpl)
        testClass = new ResultsServiceImpl(playerService, gameService)

    }

    void cleanup() {
        testClass = null
        playerService = null
        gameService = null
    }

    def "getGameResultsByGameId - when game id supplied then return a GameResultsDto for that game"() {
        given: "a game id is supplied"
        def gameId = 1
        def playerId = 1
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 2,
                target: 44,
                guesses: [new Guess(), new Guess()],
                attempts: 2)

        and: "a valid game"
        gameService.getById(gameId) >> game

        when: "a request by valid game id is sent"
        def result = testClass.getGameResultsByGameId(gameId)

        then: "a matching GameResultsDto is returned"
        result.attempts == game.attempts
        result.totalScore == game.totalScore
        result.gameId == game.id
        result.playerId == game.playerId
        result.statusInfo.message == GameStatus.IN_PROGRESS.message
        result.statusInfo.status == GameStatus.IN_PROGRESS.status
    }

    def "getPlayerResultsByGameId - when player id supplied then return a PlayerResultsDto for that player"() {
        given: "a player id is supplied"
        def playerId = 1
        def player = new Player(
                id: playerId,
                firstName: "first_name",
                lastName: "last_name",
                dateOfBirth: Instant.now().toString(),
                totalGames: 5,
                history: [
                        new Game(totalScore: 11, statusInfo: GameStatus.WON),
                        new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                        new Game(totalScore: 10, statusInfo: GameStatus.WON),
                        new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                        new Game(totalScore: 3, statusInfo: GameStatus.LOST)
                ]
        )

        and: "a player matching player id"
        playerService.getById(playerId) >> player

        when: "a request by valid player id is sent"
        def result = testClass.getPlayerResultsByPlayerId(playerId)

        then: "a matching PlayerResultsDto is returned"
        result.id == player.id
        result.firstName == player.firstName
        result.lastName == player.lastName
        result.totalGames == player.totalGames
        result.won == 2
        result.lost == 3
        result.totalScore == 30
        result.avgWinRatio == 0.4
    }

    def "getPlayersTotalResultsRanking - return all players results ranked by wins"() {
        given: "a list of all players"
        playerService.getAllPlayers() >> listOfPlayers()

        expect: "when a request for all players rank is sent then return result ordered by wins"
        def result = testClass.getPlayersTotalResultsRanking()

        with(result.playersResultsRanking) {
            player ->
                player[0].id == 1 && player[1].id == 3 && player[2].id == 2
        }


    }

    private List<Player> listOfPlayers() {
        return [
                new Player(
                        id: 1,
                        firstName: "first_name_1",
                        lastName: "last_name_1",
                        dateOfBirth: Instant.now().toString(),
                        totalGames: 5,
                        history: [
                                new Game(totalScore: 11, statusInfo: GameStatus.WON),
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                                new Game(totalScore: 10, statusInfo: GameStatus.WON),
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST)
                        ]
                ),
                new Player(
                        id: 2,
                        firstName: "first_name_2",
                        lastName: "last_name_2",
                        dateOfBirth: Instant.now().toString(),
                        totalGames: 2,
                        history: [
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                        ]
                ),
                new Player(
                        id: 3,
                        firstName: "first_name_3",
                        lastName: "last_name_3",
                        dateOfBirth: Instant.now().toString(),
                        totalGames: 4,
                        history: [
                                new Game(totalScore: 12, statusInfo: GameStatus.WON),
                                new Game(totalScore: 3, statusInfo: GameStatus.LOST),
                                new Game(totalScore: 11, statusInfo: GameStatus.WON),
                                new Game(totalScore: 2, statusInfo: GameStatus.IN_PROGRESS)
                        ]
                )

        ]
    }
}
