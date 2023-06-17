package com.xzinoviou.guessthenumber.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author : Xenofon Zinoviou
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Player {

    private Integer id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private Integer totalGames;

    private List<Game> history;
}
