package com.baibei.authserver.dto;

import lombok.Data;

@Data
public class TokenIsExpiredRequest {
    private String token;
}
