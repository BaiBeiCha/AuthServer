package com.baibei.authserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthDto {
    private String username;
    private String role;
    private String scope;
}
