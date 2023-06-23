package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.dao.DatabaseDao
import com.xzinoviou.guessthenumber.dto.game.GameStatusDto
import com.xzinoviou.guessthenumber.exception.GuessTheNumberException
import com.xzinoviou.guessthenumber.model.Game
import com.xzinoviou.guessthenumber.model.GameStatus
import com.xzinoviou.guessthenumber.model.Guess
import com.xzinoviou.guessthenumber.request.GameCreateRequest
import com.xzinoviou.guessthenumber.request.GuessRequest
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
        databaseDaoMock.getNextGameId() >> { new RuntimeException("fail to allocate next game id") }

        when: "a call to create a game is made"
        testClass.create(request)

        then: "it should throw a GuessTheNumber exception"
        def ex = thrown(GuessTheNumberException)
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
        playerServiceMock.addGameToPlayer((Integer) _, (Game) _) >> { new RuntimeException("fail to add game to player") }

        when: "a call to create a game is made"
        testClass.create(request)

        then: "it should throw a GuessTheNumber exception"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Fail to create game for player with id: " + playerId
    }

    def "getById - when request an existing game by id then return game"() {
        given: "an existing game id"
        def gameId = 1;

        and: "game retrieval succeeds"
        databaseDaoMock.getGames() >> [new Game(id: 3), new Game(id: 4), new Game(id: 1)]

        when: "a request to retrieve the game by the id"
        def result = testClass.getById(gameId)

        then: "the game retrieved should match the id"
        result.id == 1
    }

    def "getById - when requesting a non-existing game by id then throw GuessTheNumberException"() {
        given: "an existing game id"
        def gameId = 1;

        and: "game retrieval succeeds"
        databaseDaoMock.getGames() >> [new Game(id: 3), new Game(id: 4), new Game(id: 11)]

        when: "a request to retrieve the game by the id"
        testClass.getById(gameId)

        then: "the game retrieved should match the id"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Failed to retrieve game with id: " + gameId
    }

    def "guess - when correct guess then return game status won"() {
        given:
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: target)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 0,
                target: target,
                guesses: [new Guess()],
                attempts: 1)

        and:
        databaseDaoMock.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when:
        def result = testClass.guess(guessRequest)

        then:
        result.message == GameStatus.WON.message
        result.status == GameStatus.WON.status
        game.totalScore == 10
        game.attempts == 2
        game.guesses.size() == 2
        with(game.guesses) {
            guess ->
                guess[1].attempt == 2 &&
                        guess[1].score == 10
        }
    }
}
