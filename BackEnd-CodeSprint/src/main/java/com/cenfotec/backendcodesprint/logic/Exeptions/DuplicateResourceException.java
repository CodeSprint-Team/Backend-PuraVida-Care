package com.cenfotec.backendcodesprint.logic.Exeptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
