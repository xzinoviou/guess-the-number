package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.service.GameService;
import com.xzinoviou.guessthenumber.request.GuessRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Xenofon Zinoviou
 */
@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/update")
    public Game update(@RequestBody GuessRequest guessRequest) {
        return gameService.update(guessRequest);
    }

    @PostMapping
    public Game create(@RequestBody GameCreateRequest gameCreateRequest) {
        return gameService.create(gameCreateRequest);
    }
}
