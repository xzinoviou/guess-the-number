package com.xzinoviou.guessthenumber;

import lombok.Builder;
import lombok.Getter;

/**
 * @author : Xenofon Zinoviou
 */
@Builder
@Getter
public class AppError {

    private int status;

    private String message;

    private String timestamp;
}
