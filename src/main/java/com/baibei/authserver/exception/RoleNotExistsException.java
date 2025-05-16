package com.baibei.authserver.exception;

public class RoleNotExistsException extends RuntimeException {
    public RoleNotExistsException(String roleName) {
        super("Role '" + roleName + "' does not exist");
    }
}
