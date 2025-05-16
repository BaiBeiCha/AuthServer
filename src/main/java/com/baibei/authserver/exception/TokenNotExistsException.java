package com.baibei.authserver.exception;

public class TokenNotExistsException extends RuntimeException {
    public TokenNotExistsException(String token) {
        super("Token <" + token + "> not exists");
    }
}
