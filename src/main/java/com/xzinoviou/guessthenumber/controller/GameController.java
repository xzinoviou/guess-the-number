package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.dto.GameStatusInfoDto;
import com.xzinoviou.guessthenumber.dto.GameResultsDto;
import com.xzinoviou.guessthenumber.request.GameCreateRequest;
import com.xzinoviou.guessthenumber.service.GameService;
import com.xzinoviou.guessthenumber.request.GuessRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
        return ResponseEntity.ok(gameService.create(gameCreateRequest));
    }

    @PostMapping("/update")
    public ResponseEntity<GameStatusInfoDto> update(@RequestBody GuessRequest guessRequest) {
        return new ResponseEntity<>(gameService.update(guessRequest), HttpStatus.OK);
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<GameResultsDto> getGameResultsById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(gameService.getGameResultsById(id));
    }
}
