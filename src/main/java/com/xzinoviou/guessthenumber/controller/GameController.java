package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.dto.GameResultsDto;
import com.xzinoviou.guessthenumber.model.GameMessage;
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
    public GameMessage create(@RequestBody GameCreateRequest gameCreateRequest) {
        return gameService.create(gameCreateRequest);
    }

    @PostMapping("/update")
    public GameMessage update(@RequestBody GuessRequest guessRequest) {
        return gameService.update(guessRequest);
    }

    @GetMapping("/results/{id}")
    public GameResultsDto getGameResultsById(@PathVariable("id") Integer id){
        return gameService.getGameResultsById(id);
    }
}
