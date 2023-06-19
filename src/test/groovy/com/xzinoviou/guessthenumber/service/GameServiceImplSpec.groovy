package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.dao.DatabaseDao
import com.xzinoviou.guessthenumber.exception.GuessTheNumberException
import com.xzinoviou.guessthenumber.model.Game
import com.xzinoviou.guessthenumber.model.GameStatus
import com.xzinoviou.guessthenumber.request.GameCreateRequest
import spock.lang.Specification

/**
 * @author : Xenofon Zinoviou
 */
class GameServiceImplSpec extends Specification {

    private DatabaseDao databaseDaoMock
    private PlayerService playerServiceMock
    private GameServiceImpl testClass


    void setup() {
        databaseDaoMock = Mock()
        playerServiceMock = Mock(PlayerServiceImpl)
        testClass = new GameServiceImpl(databaseDaoMock, playerServiceMock)
    }

    void cleanup() {
        testClass = null
        databaseDaoMock = null
        playerServiceMock = null
    }

    def "create - when a game is created successfully then return game status created"() {
        given: "A playerId and a game request supplied"
        def gameId = 1
        def playerId = 100
        def request = new GameCreateRequest(playerId: playerId)

        and: "an id for the next game is allocated by the database"
        databaseDaoMock.getNextGameId() >> gameId

        when: "a call to create a game is made"
        def result = testClass.create(request)

        then: "it should return a game status of created"
        result.message == GameStatus.CREATED.message
        result.status == GameStatus.CREATED.status
        1 * playerServiceMock.addGameToPlayer(id -> {
            id == playerId
        }, game -> {
            game.id == gameId
            game.attempts == 3
            game.guesses == []
            game.target <= 100
            game.totalScore == 0
            game.statusInfo == GameStatus.CREATED
        })
    }

    def "create - when next game id fails to allocate then throw GuessTheNumberException"() {
        given: "A playerId and a game request supplied"
        def playerId = 100
        def request = new GameCreateRequest(playerId: playerId)

        and: "next game id allocation fails"
        databaseDaoMock.getNextGameId() >> new RuntimeException("fail to allocate next game id")

        when: "a call to create a game is made"
        def ex = testClass.create(request)

        then: "it should throw a GuessTheNumber exception"
        ex = thrown(GuessTheNumberException)
        ex.message == "Fail to create game for player with id: " + playerId

    }

    def "create - when adding game to player fails then throw GuessTheNumberException"() {
        given: "A playerId and a game request supplied"
        def gameId = 1
        def playerId = 100
        def request = new GameCreateRequest(playerId: playerId)

        and: "next game id allocation fails"
        databaseDaoMock.getNextGameId() >> gameId

        and: "adding game to player fails"
        playerServiceMock.addGameToPlayer((Integer) _, (Game) _) >> new RuntimeException("fail to add game to player")

        when: "a call to create a game is made"
        def ex = testClass.create(request)

        then: "it should throw a GuessTheNumber exception"
        ex = thrown(GuessTheNumberException)
        ex.message == "Fail to create game for player with id: " + playerId

    }
}
