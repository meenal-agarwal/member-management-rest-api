package com.member.task.service;

import com.member.task.exceptions.MemberKeyAlreadyAvalilableException;
import com.member.task.exceptions.MemberNotAvailableException;
import com.member.task.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    @Qualifier("memberStore")
    private Map<String, Member> memberStore;

    @Override
    public void createMember(Member member) {
        String key = member.getLastName() + member.getDob();
        if (memberStore.containsKey(key))
            throw new MemberKeyAlreadyAvalilableException("Member already available");
        memberStore.put(key, member);
    }

    @Override
    public Member readMember(String lastName, String dob) {
        String key = lastName + dob;
        if (!memberStore.containsKey(key))
            throw new MemberNotAvailableException("Member is not available");
        return memberStore.get(key);
    }

    @Override
    public void updateMember(String lastName, String dob, Member member) {
        String key = lastName + dob;
        if (!memberStore.containsKey(key))
            throw new MemberNotAvailableException("Member is not available");
        // Removing Existing Member
        memberStore.remove(key);
        // Adding new Members (Key could be updated).
        String newKey = member.getLastName() + member.getDob();
        memberStore.put(newKey, member);
    }

    @Override
    public void deleteMember(String lastName, String dob) {
        String key = lastName + dob;
        if (!memberStore.containsKey(key))
            throw new MemberNotAvailableException("Member is not available");
        memberStore.remove(key);
    }

    @Override
    public List<Member> listMembers() {
        if (memberStore.isEmpty())
            throw new MemberNotAvailableException("No Member Present");
        return new ArrayList<Member>(memberStore.values());
    }
}
