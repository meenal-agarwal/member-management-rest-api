package com.member.task.controller;

import com.google.gson.Gson;
import com.member.task.exceptions.MemberKeyAlreadyAvalilableException;
import com.member.task.exceptions.MemberNotAvailableException;
import com.member.task.model.Member;
import com.member.task.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ServiceController.class)
public class ServiceControllerTest {


    private final Member member = new Member("John", "Snow", "1990.03.01", 12345);
    private final Member member1 = new Member("Johny", "Winter", "1990.04.01", 12345);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    public void verifyCreateMemberValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/memberService/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"John\", \"lastName\" : \"Snow\", \"dob\" : \"1990.03.01\", \"postalCode\" : 12345  }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":201,\"status\":\"Created\"}", true));
    }

    @Test
    public void verifyCreateMemberAlreadyPresent() throws Exception {
        Gson gson = new Gson();
        doThrow(new MemberKeyAlreadyAvalilableException("Member already available")).when(memberService).createMember(member);
        mockMvc.perform(MockMvcRequestBuilders.put("/memberService/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(member))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value("ALREADY_REPORTED"))
                .andExpect(jsonPath("$.message").value("Member already available"));
        ;
    }

    @Test
    public void verifyCreateMemberMissingJsonData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/memberService/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"John\", \"lastName\" : \"Snow\", \"dob\" : \"1990.03.01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyCreateMemberJsonFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/memberService/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : John, \"lastName\" : \"Snow\", \"dob\" : \"1990.03.01\",\"postalCode\" : 12345 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyCreateMemberInvalidDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/memberService/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"John\", \"lastName\" : \"Snow\", \"dob\" : \"1990.03.0112\", \"postalCode\" : 12345  }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().json("{\"status\":\"NOT_ACCEPTABLE\",\"message\":\"DOB Format should be yyyy.MM.dd\"}", true));
    }

    @Test
    public void verifyReadMemberValidRequest() throws Exception {
        when(memberService.readMember("Snow", "1990.03.01")).thenReturn(member);
        mockMvc.perform(MockMvcRequestBuilders.get("/memberService/member/1990.03.01/Snow").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.dob").exists())
                .andExpect(jsonPath("$.postalCode").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Snow"))
                .andExpect(jsonPath("$.dob").value("1990.03.01"))
                .andExpect(jsonPath("$.postalCode").value("12345"));
    }

    @Test
    public void verifyReadMemberNotPresent() throws Exception {
        when(memberService.readMember("Snow", "1990.03.01")).thenThrow(new MemberNotAvailableException("Member Not Found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/memberService/member/1990.03.01/Snow").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Member Not Found"));
    }

    @Test
    public void verifyReadMemberInvalidDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/memberService/member/1990.03.01112/Snow")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().json("{\"status\":\"NOT_ACCEPTABLE\",\"message\":\"DOB Format should be yyyy.MM.dd\"}", true));
    }

    @Test
    public void verifyUpdateMemberValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/memberService/update/1990.03.01/Snow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Johny\", \"lastName\" : \"Winter\", \"dob\" : \"1990.03.01\", \"postalCode\" : 12345  }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    public void verifyUpdateMemberNotPresent() throws Exception {
        Gson gson = new Gson();
        doThrow(new MemberNotAvailableException("Member Not Found")).when(memberService).updateMember("Snow123", "1990.03.01", member);
        mockMvc.perform(MockMvcRequestBuilders.post("/memberService/update/1990.03.01/Snow123").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(member)))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Member Not Found"));
    }

    @Test
    public void verifyUpdateMemberInvalidDateFormatURL() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/memberService/update/1990.13.01/Snow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Johny\", \"lastName\" : \"Winter\", \"dob\" : \"1990.03.01\", \"postalCode\" : 10000  }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"NOT_ACCEPTABLE\",\"message\":\"DOB Format should be yyyy.MM.dd\"}", true));
    }

    @Test
    public void verifyUpdateMemberInvalidDateFormatJSON() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/memberService/update/1990.03.01/Snow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Johny\", \"lastName\" : \"Winter\", \"dob\" : \"1990.13.01\", \"postalCode\" : 10000  }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":\"NOT_ACCEPTABLE\",\"message\":\"DOB Format should be yyyy.MM.dd\"}", true));
    }

    @Test
    public void verifyUpdateMemberJsonMissingData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/memberService/update/1990.03.01/Snow")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Johny\", \"lastName\" : \"Winter\", \"dob\" : \"1990.03.01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyDeleteMemberValidRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/memberService/delete/1990.03.01/Snow")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    public void verifyDeleteMemberNotPresent() throws Exception {
        doThrow(new MemberNotAvailableException("Member Not Found")).when(memberService).deleteMember("Snow123", "1990.03.01");
        mockMvc.perform(MockMvcRequestBuilders.delete("/memberService/delete/1990.03.01/Snow123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Member Not Found"));
    }

    @Test
    public void verifyDeleteMemberInvalidDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/memberService/delete/03.01.1990/Snow")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().json("{\"status\":\"NOT_ACCEPTABLE\",\"message\":\"DOB Format should be yyyy.MM.dd\"}", true));
    }

    @Test
    public void verifyListMembersValidRequest() throws Exception {
        List<Member> stub = new ArrayList<>(Arrays.asList(member, member1));
        when(memberService.listMembers()).thenReturn(stub);
        mockMvc.perform(MockMvcRequestBuilders.get("/memberService/memberList").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].dob").exists())
                .andExpect(jsonPath("$[0].postalCode").exists())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Snow"))
                .andExpect(jsonPath("$[0].dob").value("1990.03.01"))
                .andExpect(jsonPath("$[0].postalCode").value("12345"))
                .andExpect(jsonPath("$[1].firstName").exists())
                .andExpect(jsonPath("$[1].lastName").exists())
                .andExpect(jsonPath("$[1].dob").exists())
                .andExpect(jsonPath("$[1].postalCode").exists())
                .andExpect(jsonPath("$[1].firstName").value("Johny"))
                .andExpect(jsonPath("$[1].lastName").value("Winter"))
                .andExpect(jsonPath("$[1].dob").value("1990.04.01"))
                .andExpect(jsonPath("$[1].postalCode").value("12345"));
    }

    @Test
    public void verifyEmptyListMembers() throws Exception {
        when(memberService.listMembers()).thenThrow(new MemberNotAvailableException("Member Not Found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/memberService/memberList").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Member Not Found"));
    }

}