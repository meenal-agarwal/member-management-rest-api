package com.member.task.service;

import com.member.task.exceptions.MemberKeyAlreadyAvalilableException;
import com.member.task.exceptions.MemberNotAvailableException;
import com.member.task.model.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Mock
    Map<String, Member> memberStore;
    Member member1 = new Member("John", "Snow", "1990.03.01", 25412);
    Member member2 = new Member("Johny", "Winter", "2006.03.04", 10045);
    @InjectMocks
    @Autowired
    private MemberService memberService;

    @Test
    public void testListMembers() {
        List<Member> stub = new ArrayList<>(Arrays.asList(member1, member2));
        when(memberStore.isEmpty()).thenReturn(false);
        when(memberStore.values()).thenReturn(stub);
        List<Member> result = memberService.listMembers();
        assertEquals(2, result.size());
    }

    @Test(expected = MemberNotAvailableException.class)
    public void testListMembersNoMembers() {
        when(memberStore.isEmpty()).thenReturn(true);
        memberService.listMembers();
    }

    @Test
    public void testCreateMember() throws Exception {
        when(memberStore.containsKey("Snow1990.03.01")).thenReturn(false);
        memberService.createMember(member1);
        verify(memberStore, times(1)).put("Snow1990.03.01", member1);
    }

    @Test(expected = MemberKeyAlreadyAvalilableException.class)
    public void testCreateMemberAlreadyPresent() throws Exception {
        when(memberStore.containsKey("Snow1990.03.01")).thenReturn(true);
        memberService.createMember(member1);
    }

    @Test
    public void testReadMember() throws Exception {
        when(memberStore.containsKey("Winter2006.03.04")).thenReturn(true);
        when(memberStore.get("Winter2006.03.04")).thenReturn(member2);
        Member result = memberService.readMember("Winter", "2006.03.04");
        assertEquals(result.getFirstName(), "Johny");
        assertEquals(Integer.valueOf(10045), result.getPostalCode());
    }

    @Test(expected = MemberNotAvailableException.class)
    public void testReadMembersNoMembers() {
        when(memberStore.containsKey("Winny2006.03.04")).thenReturn(false);
        memberService.readMember("Winny", "2006.03.04");
    }

    @Test
    public void testUpdateMember() throws Exception {
        when(memberStore.containsKey("Snow1990.03.01")).thenReturn(true);
        memberService.updateMember("Snow", "1990.03.01", member2);
        verify(memberStore, times(1)).remove("Snow1990.03.01");
        verify(memberStore, times(1)).put("Winter2006.03.04", member2);
    }

    @Test(expected = MemberNotAvailableException.class)
    public void testUpdateMembersNoMembers() {
        when(memberStore.containsKey("Winny2006.03.04")).thenReturn(false);
        memberService.updateMember("Winny", "2006.03.04", member1);
    }

    @Test
    public void testDeleteMember() throws Exception {
        when(memberStore.containsKey("Winter2006.03.04")).thenReturn(true);
        memberService.deleteMember("Winter", "2006.03.04");
        verify(memberStore, times(1)).remove("Winter2006.03.04");
    }

    @Test(expected = MemberNotAvailableException.class)
    public void testDeleteMembersNoMembers() {
        when(memberStore.containsKey("Winny2006.03.04")).thenReturn(false);
        memberService.deleteMember("Winny", "2006.03.04");
    }

}