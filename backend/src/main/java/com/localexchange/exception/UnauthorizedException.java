package com.localexchange.exception;

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("Vous n'êtes pas autorisé à effectuer cette action");
    }
}