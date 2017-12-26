package com.member.task.exceptions;

public class DateOfBirthFormatNotValidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DateOfBirthFormatNotValidException(String message) {
        super(message);
    }

}
