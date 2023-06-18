package com.xzinoviou.guessthenumber.dto.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameStatusDto {

    private String status;
    private String message;
}
