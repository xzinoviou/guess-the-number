package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.model.Game;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.service.GameService;
import com.xzinoviou.guessthenumber.request.GuessRequest;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Game create(@RequestBody GameCreateRequest gameCreateRequest) {
        return gameService.create(gameCreateRequest);
    }

    @PostMapping("/update")
    public Game update(@RequestBody GuessRequest guessRequest) {
        return gameService.update(guessRequest);
    }

    @GetMapping("/results/{id}")
    public Game getGameResultsById(@PathVariable("id") Integer id){
        return gameService.getGameResultsById(id);
    }
}
