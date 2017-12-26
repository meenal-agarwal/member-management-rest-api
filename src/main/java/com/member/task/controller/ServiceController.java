package com.member.task.controller;

import com.member.task.exceptions.DateOfBirthFormatNotValidException;
import com.member.task.helper.DateValidator;
import com.member.task.model.HTTPResponse;
import com.member.task.model.Member;
import com.member.task.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/memberService")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private MemberService memberService;

    @RequestMapping(method = RequestMethod.PUT, value = "/member")
    @ResponseBody
    public HTTPResponse createMember(@Valid @RequestBody Member member) {
        logger.info("Created new Member");
        if (!DateValidator.isThisDateValid(member.getDob())) {
            throw new DateOfBirthFormatNotValidException("DOB Format should be yyyy.MM.dd");
        }
        memberService.createMember(member);
        logger.info(String.format("New Member Created with D.O.B : %s and  Lastname : %s", member.getDob(), member.getLastName()));
        return new HTTPResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());

    }

    @RequestMapping(method = RequestMethod.GET, value = "/member/{dob}/{lastName}")
    @ResponseBody
    public Member readMember(@PathVariable("dob") String dob, @PathVariable("lastName") String lastName) {
        logger.info("Retrieving Member Information");
        if (!DateValidator.isThisDateValid(dob)) {
            throw new DateOfBirthFormatNotValidException("DOB Format should be yyyy.MM.dd");
        }
        return memberService.readMember(lastName, dob);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update/{dob}/{lastName}")
    @ResponseBody
    public HTTPResponse updateMember(@PathVariable("dob") String dob, @PathVariable("lastName") String lastName,
                                     @Valid @RequestBody Member member) {
        logger.info("Updating Member Information");
        if (!(DateValidator.isThisDateValid(dob) && DateValidator.isThisDateValid(member.getDob()))) {
            throw new DateOfBirthFormatNotValidException("DOB Format should be yyyy.MM.dd");
        }
        memberService.updateMember(lastName, dob, member);
        logger.info(String.format("Member information updated with D.O.B : %s and  Lastname : %s", member.getDob(), member.getLastName()));
        return new HTTPResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{dob}/{lastName}")
    @ResponseBody
    public HTTPResponse deleteMember(@PathVariable("dob") String dob, @PathVariable("lastName") String lastName) {
        logger.info("Deleting Member Information");
        if (!DateValidator.isThisDateValid(dob)) {
            throw new DateOfBirthFormatNotValidException("DOB Format should be yyyy.MM.dd");
        }
        memberService.deleteMember(lastName, dob);
        logger.info(String.format("Member information deleted for D.O.B : %s and  Lastname : %s", dob, lastName));
        return new HTTPResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/memberList")
    @ResponseBody
    public List<Member> listMembers() {
        logger.info("Listing Member Information");
        return memberService.listMembers();
    }
}
