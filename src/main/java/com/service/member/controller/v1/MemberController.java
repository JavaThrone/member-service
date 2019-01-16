package com.service.member.controller.v1;

import com.service.member.model.Member;
import com.service.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/member/")
public class MemberController {

    private final MemberService memberService;
    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Member>> listAllMembers() {
        logger.info("Fetching all Members");

        List<Member> members = memberService.findAll();

        if (members.isEmpty()) {
            logger.info("Unable to get list of Members. Members not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping(params = "id", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE })
    public ResponseEntity<Member> getMember(@PathVariable("id") String id) throws IOException {
        logger.info("Fetching Member with id " + id);

        Member member = memberService.findOne(id);

        if (member == null) {
            logger.info("Unable to get. Member with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE })
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        logger.info("Creating Member " + member);
        Member savedMember = memberService.save(member);
        return new ResponseEntity<>(savedMember, HttpStatus.OK);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE })
    public ResponseEntity<Void> updateMember(@RequestBody Member member) throws IOException {
        logger.info("Updating Member " + member.getId());

        Member updatedMember = memberService.findOne(member.getId());

        if (updatedMember == null) {
            logger.info("Unable to update. Member with id " + member.getId() + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        memberService.update(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(params = "id", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE }, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_ATOM_XML_VALUE })
    public ResponseEntity<Void> deleteMember(@PathVariable("id") String id) throws IOException {
        logger.info("Deleting Member with id " + id);

        Member deletedMember = memberService.findOne(id);

        if (deletedMember == null) {
            logger.info("Unable to delete. Member with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

