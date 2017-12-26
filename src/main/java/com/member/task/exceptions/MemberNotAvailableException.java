package com.member.task.exceptions;

public class MemberNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MemberNotAvailableException(String message) {
        super(message);
    }

}
