package com.member.task.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Member {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String dob;

    @NotNull
    private Integer postalCode;

    public Member() {
    }

    public Member(String firstName, String lastName, String dob, Integer postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.postalCode = postalCode;
    }
}
