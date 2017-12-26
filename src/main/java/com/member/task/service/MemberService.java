package com.member.task.service;

import com.member.task.model.Member;

import java.util.List;

public interface MemberService {

    void createMember(Member member);

    Member readMember(String lastName, String dob);

    void updateMember(String lastName, String dob, Member member);

    void deleteMember(String lastName, String dob);

    List<Member> listMembers();
}