package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.model.Player;
import com.xzinoviou.guessthenumber.request.PlayerCreateRequest;
import com.xzinoviou.guessthenumber.service.PlayerService;
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
    public Player getById(@PathVariable Integer id) {
        return playerService.getById(id);
    }

    @PostMapping
    public Player createPlayer(@RequestBody PlayerCreateRequest playerCreateRequest) {
        return playerService.create(playerCreateRequest);
    }
}
