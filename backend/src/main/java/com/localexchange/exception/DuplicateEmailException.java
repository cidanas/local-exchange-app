package com.localexchange.exception;

public class DuplicateEmailException extends RuntimeException {
    
    public DuplicateEmailException(String email) {
        super(String.format("Un compte existe déjà avec l'email : %s", email));
    }
    
    public DuplicateEmailException() {
        super("Cet email est déjà utilisé");
    }
}