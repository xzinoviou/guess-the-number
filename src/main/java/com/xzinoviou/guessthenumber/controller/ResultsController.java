package com.xzinoviou.guessthenumber.controller;

import com.xzinoviou.guessthenumber.dto.results.GameResultsDto;
import com.xzinoviou.guessthenumber.dto.results.PlayerResultsDto;
import com.xzinoviou.guessthenumber.dto.results.TotalResultsDto;
import com.xzinoviou.guessthenumber.service.ResultsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Xenofon Zinoviou
 */
@RestController
@RequestMapping("/results")
public class ResultsController {

    private final ResultsService resultsService;

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<GameResultsDto> getGameResultsById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(resultsService.getGameResultsById(id));
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<PlayerResultsDto> getPlayerResultsById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(resultsService.getPlayerResultsById(id));
    }

    @GetMapping("/players")
    public ResponseEntity<TotalResultsDto> getPlayersTotalResultsRanking() {
        return ResponseEntity.ok(resultsService.getPlayersTotalResultsRanking());
    }

}
