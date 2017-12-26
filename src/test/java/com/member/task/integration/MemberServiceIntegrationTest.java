package com.member.task.integration;

import com.member.task.model.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberServiceIntegrationTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void createClientValid() {
        Member member1 = new Member("David", "Mathew", "2005.12.1", 10002);
        HttpEntity<Member> entity = new HttpEntity<Member>(member1, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/member"),
                        HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void createClientAlreadyPresent() {
        Member member1 = new Member("John", "Snow", "1990.03.01", 12345);
        HttpEntity<Member> entity = new HttpEntity<Member>(member1, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/member"),
                        HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.ALREADY_REPORTED, responseEntity.getStatusCode());
    }

    @Test
    public void updateMemberValid() {
        Member member1 = new Member("Bill", "Snow", "1990.03.01", 12345);
        HttpEntity<Member> entity = new HttpEntity<Member>(member1, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/update/1990.03.01/Snow"),
                        HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void updateMemberNotPresent() {
        Member member1 = new Member("Wilson", "Snow", "1990.03.01", 12345);
        HttpEntity<Member> entity = new HttpEntity<Member>(member1, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/update/1990.03.02/Bill"),
                        HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void readMemberValid() {
        HttpEntity<Member> entity = new HttpEntity<Member>(null, headers);
        ResponseEntity<Member> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/member/2006.03.04/Winter"),
                        HttpMethod.GET, entity, Member.class);
        assertEquals("Johny", responseEntity.getBody().getFirstName());
        assertEquals(Integer.valueOf(10045), responseEntity.getBody().getPostalCode());
    }

    @Test
    public void readMemberNotPresent() {
        HttpEntity<Member> entity = new HttpEntity<Member>(null, headers);
        ResponseEntity<Member> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/member/2006.03.04/Bill"),
                        HttpMethod.GET, entity, Member.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteMemberValid() {
        HttpEntity<Member> entity = new HttpEntity<Member>(null, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/delete/2006.03.04/Winter"),
                        HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void deleteMemberNotPresent() {
        HttpEntity<Member> entity = new HttpEntity<Member>(null, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/delete/2006.03.04/Bill"),
                        HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void listAllMemberValid() {
        HttpEntity<Member> entity = new HttpEntity<Member>(null, headers);
        ResponseEntity<List<Member>> responseEntity =
                restTemplate.exchange(
                        createURLWithPort("/memberService/memberList"),
                        HttpMethod.GET, entity, new ParameterizedTypeReference<List<Member>>() {
                        });
        assertEquals(2, responseEntity.getBody().size());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @TestConfiguration
    static class MemberServiceConfiguration {
        @Bean
        public Map<String, Member> memberStore() {
            Map<String, Member> memberStore = new HashMap<>();
            Member member1 = new Member("John", "Snow", "1990.03.01", 12345);
            Member member2 = new Member("Johny", "Winter", "2006.03.04", 10045);
            memberStore.put("Snow1990.03.01", member1);
            memberStore.put("Winter2006.03.04", member2);
            return memberStore;
        }
    }
}