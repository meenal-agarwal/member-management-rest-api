package com.member.task.exceptions;

public class MemberKeyAlreadyAvalilableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MemberKeyAlreadyAvalilableException(String message) {
        super(message);
    }

}
