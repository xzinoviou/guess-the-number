package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.model.Game
import com.xzinoviou.guessthenumber.model.GameStatus
import com.xzinoviou.guessthenumber.model.Guess
import spock.lang.Specification

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
}
