package com.parley.testing.exceptions;

public class ValueNotFoundException extends AssertionError {
    public ValueNotFoundException(String message) {
        super(message);
    }
}
