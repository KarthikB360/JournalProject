package com.user.exception;

public class CustomBadRequestException extends CustomException {

    private static final long serialVersionUID = 1L;

    public CustomBadRequestException(String message) {
        super(message);
    }
}
