package com.service.member.service.impl;

import com.service.member.model.File;
import com.service.member.model.Member;
import com.service.member.service.MemberService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceImplTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void testFindAll() throws IOException {

        List<Member> beforeSavedMembers = memberService.findAll();

        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        memberService.save(member1);


        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "777", null);
        memberService.save(member2);

        List<Member> afterSavedMembers = memberService.findAll();

        int numberSavedMembers = 2;
        assertNotEquals(beforeSavedMembers.size(), afterSavedMembers.size());
        assertEquals(beforeSavedMembers.size() + numberSavedMembers, afterSavedMembers.size());

    }

    @Test
    public void testFindOne() throws IOException {


        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        Member savedMember1 = memberService.save(member1);
        Member foundMember1 = memberService.findOne(savedMember1.getId());
        assertEquals(savedMember1.getId(), foundMember1.getId());

        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "777", null);
        Member savedMember2 = memberService.save(member2);
        Member foundMember2 = memberService.findOne(savedMember2.getId());
        assertEquals(savedMember2.getId(), foundMember2.getId());

    }

    @Test
    public void testSave() throws IOException {


        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "777", null);
        assertNull(member1.getId());
        assertNull(member2.getId());

        memberService.save(member1);
        memberService.save(member2);
        assertNotNull(member1.getId());
        assertNotNull(member2.getId());

    }

    @Test
    public void testUpdate() throws IOException {

        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        Member editMember1 = new Member("EditedMember1FirstName", "EditedMember1LastName", LocalDate.now().plusDays(1), "333", new File("EditedTest", "EditedTest", IOUtils.toByteArray(stream)));
        Member savedMember1 = memberService.save(member1);

        editMember1.setId(savedMember1.getId());

        memberService.update(editMember1);
        Member editedMember1 = memberService.findOne(editMember1.getId());
        assertEquals(savedMember1.getId(), editedMember1.getId());
        assertNotEquals(savedMember1.getFirstName(), editedMember1.getFirstName());
        assertNotEquals(savedMember1.getLastName(), editedMember1.getLastName());
        assertNotEquals(savedMember1.getDateOfBirth(), editedMember1.getDateOfBirth());
        assertNotEquals(savedMember1.getPostalCode(), editedMember1.getPostalCode());

        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "911", null);
        Member editMember2 = new Member("EditedMember2FirstName", "EditedMember2LastName", LocalDate.now().plusDays(1), "333", null);
        Member savedMember2 = memberService.save(member2);

        editMember2.setId(savedMember2.getId());

        memberService.update(editMember2);
        Member editedMember2 = memberService.findOne(editMember2.getId());
        assertEquals(savedMember2.getId(), editedMember2.getId());
        assertNotEquals(savedMember2.getFirstName(), editedMember2.getFirstName());
        assertNotEquals(savedMember2.getLastName(), editedMember2.getLastName());
        assertNotEquals(savedMember2.getDateOfBirth(), editedMember2.getDateOfBirth());
        assertNotEquals(savedMember2.getPostalCode(), editedMember2.getPostalCode());

    }

    @Test
    public void testDelete() throws IOException {

        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        Member savedMember1 = memberService.save(member1);
        assertNotNull(memberService.findOne(savedMember1.getId()));
        memberService.delete(savedMember1.getId());
        assertNull(memberService.findOne(savedMember1.getId()));


        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "777", null);
        Member savedMember2 = memberService.save(member2);
        assertNotNull(memberService.findOne(savedMember2.getId()));
        memberService.delete(savedMember2.getId());
        assertNull(memberService.findOne(savedMember2.getId()));
    }
}