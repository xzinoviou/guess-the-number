package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.PlayerCreateRequest;
import com.xzinoviou.guessthenumber.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Xenofon Zinoviou
 */
@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(playerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerCreateRequest playerCreateRequest) {
        return new ResponseEntity<>(playerService.create(playerCreateRequest), HttpStatus.CREATED);
    }
}
