package com.xzinoviou.guessthenumber.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@AllArgsConstructor
public class GameStatusInfoDto {

    private String status;
    private String message;
}
