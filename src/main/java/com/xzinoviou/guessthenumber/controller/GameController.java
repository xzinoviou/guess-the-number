package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.dto.game.GameStatusInfoDto;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.request.GuessRequest;
import com.xzinoviou.guessthenumber.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GameStatusInfoDto> create(@RequestBody GameCreateRequest gameCreateRequest) {
        return new ResponseEntity<>(gameService.create(gameCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<GameStatusInfoDto> update(@RequestBody GuessRequest guessRequest) {
        return new ResponseEntity<>(gameService.update(guessRequest), HttpStatus.OK);
    }
}
