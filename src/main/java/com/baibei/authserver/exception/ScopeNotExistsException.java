package com.baibei.authserver.exception;

public class ScopeNotExistsException extends RuntimeException {
    public ScopeNotExistsException(String scopeName) {
        super("Scope '" + scopeName + "' not exists");
    }
}
