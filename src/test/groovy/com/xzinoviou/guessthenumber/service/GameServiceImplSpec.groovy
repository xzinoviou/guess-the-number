package com.xzinoviou.guessthenumber.service

import com.xzinoviou.guessthenumber.dao.DatabaseDao
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

    private DatabaseDao databaseDao
    private PlayerService playerService
    private GameServiceImpl testClass


    void setup() {
        databaseDao = Mock()
        playerService = Mock(PlayerServiceImpl)
        testClass = new GameServiceImpl(databaseDao, playerService)
    }

    void cleanup() {
        testClass = null
        databaseDao = null
        playerService = null
    }

    def "create - when a game is created successfully then return game status created"() {
        given: "A playerId and a game request supplied"
        def gameId = 1
        def playerId = 100
        def request = new GameCreateRequest(playerId: playerId)

        and: "an id for the next game is allocated by the database"
        databaseDao.getNextGameId() >> gameId

        when: "a call to create a game is made"
        def result = testClass.create(request)

        then: "it should return a game status of created"
        result.message == GameStatus.CREATED.message
        result.status == GameStatus.CREATED.status
        1 * playerService.addGameToPlayer(id -> {
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
        databaseDao.getNextGameId() >> { new RuntimeException("fail to allocate next game id") }

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
        databaseDao.getNextGameId() >> gameId

        and: "adding game to player fails"
        playerService.addGameToPlayer((Integer) _, (Game) _) >> { new RuntimeException("fail to add game to player") }

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
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), new Game(id: 1)]

        when: "a request to retrieve the game by the id"
        def result = testClass.getById(gameId)

        then: "the game retrieved should match the id"
        result.id == 1
    }

    def "getById - when requesting a non-existing game by id then throw GuessTheNumberException"() {
        given: "an existing game id"
        def gameId = 1;

        and: "game retrieval succeeds"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), new Game(id: 11)]

        when: "a request to retrieve the game by the id"
        testClass.getById(gameId)

        then: "the game retrieved should match the id"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Failed to retrieve game with id: " + gameId
    }

    def "guess - when correct guess then return game status won"() {
        given: "a game id, a player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: target)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 1,
                target: target,
                guesses: [new Guess()],
                attempts: 1)

        and: "a list of games"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "a correct guess request is sent"
        def result = testClass.guess(guessRequest)

        then: "the game ends successfully with game status won"
        result.message == GameStatus.WON.message
        result.status == GameStatus.WON.status
        game.totalScore == 11
        game.attempts == 2
        game.guesses.size() == 2
        with(game.guesses) {
            guess ->
                guess[1].attempt == 2 &&
                        guess[1].score == 10
        }
    }

    def "guess - when wrong guess then return game status in progress"() {
        given: "a game id, a player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 1,
                target: target,
                guesses: [new Guess()],
                attempts: 1)

        and: "a list of games"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "an incorrect guess request is sent"
        def result = testClass.guess(guessRequest)

        then: "game does not end and game status remains in progress"
        result.message == GameStatus.IN_PROGRESS.message
        result.status == GameStatus.IN_PROGRESS.status
        game.totalScore == 2
        game.attempts == 2
        game.guesses.size() == 2
        with(game.guesses) {
            guess ->
                guess[1].attempt == 2 && guess[1].guessTarget == 50
        }
    }

    def "guess - when third & final guess is wrong then return game status lost"() {
        given: "a game id, a player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 2,
                target: target,
                guesses: [new Guess(), new Guess()],
                attempts: 2)

        and: "a list of games"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: " a final guess request is sent"
        def result = testClass.guess(guessRequest)

        then: " game ends unsuccessfully with game status lost"
        result.message == GameStatus.LOST.message
        result.status == GameStatus.LOST.status
        game.totalScore == 3
        game.attempts == 3
        game.guesses.size() == 3
        with(game.guesses) {
            guess ->
                guess[2].attempt == 3 && guess[2].guessTarget == 50
        }
    }

    def "guess - when guess with invalid game id send then should throw GuessTheNumberException"() {
        given: "an invalid game id, a player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: 99, playerId: 1, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 2,
                target: target,
                guesses: [new Guess(), new Guess()],
                attempts: 2)

        and: "a list of games excluding the game with invalid supplied game id"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "a guess request is sent"
        testClass.guess(guessRequest)

        then: "a GuessTheNumber exception is thrown"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Failed to retrieve game with id: " + 99
    }

    def "guess - when guess with invalid player id send then should throw GuessTheNumberException"() {
        given: "a game id, an invalid player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: 1, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                totalScore: 2,
                target: target,
                guesses: [new Guess(), new Guess()],
                attempts: 2)

        and: "a list of games excluding the game with invalid player id supplied"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "a guess request is sent"
        testClass.guess(guessRequest)

        then: "a GuessTheNumber exception is thrown"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Failed to update game due to invalid data provided"
    }

    def "guess - when guess send for finished game then should throw GuessTheNumberException"() {
        given: "a game id, an player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.LOST,
                totalScore: 3,
                target: target,
                guesses: [new Guess(), new Guess(), new Guess()],
                attempts: 3)

        and: "a list of games including the finished game with supplied game id"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "a guess request is send"
        testClass.guess(guessRequest)

        then: "a GuessTheNumber exception is thrown"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Game is already finished"
    }

    def "guess - when guess fails to update game then should throw GuessTheNumberException"() {
        given: "a game id, an player id and a target number"
        def gameId = 1
        def playerId = 100
        def target = 44
        def guessRequest = new GuessRequest(gameId: gameId, playerId: playerId, guessTarget: 50)
        def game = new Game(
                id: gameId,
                playerId: playerId,
                statusInfo: GameStatus.IN_PROGRESS,
                target: target,
                guesses: [new Guess(), new Guess()],
                attempts: 2)

        and: "a list of games"
        databaseDao.getGames() >> [new Game(id: 3), new Game(id: 4), game]

        when: "a guess request is send"
        testClass.guess(guessRequest)

        then: "fails to update the game and a GuessTheNumber exception is thrown"
        def ex = thrown(GuessTheNumberException)
        ex.message == "Failed to update game for player with id: " + playerId
    }
}
