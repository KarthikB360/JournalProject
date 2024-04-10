package com.user.exception;

public class CustomNotFoundException extends CustomException {

    private static final long serialVersionUID = 1L;

    public CustomNotFoundException(String message) {
        super(message);
    }
}
