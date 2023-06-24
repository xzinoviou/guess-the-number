package com.xzinoviou.guessthenumber.service

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
}
