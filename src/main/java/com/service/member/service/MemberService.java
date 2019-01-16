package com.service.member.service;

import com.service.member.model.Member;

import java.io.IOException;
import java.util.List;

public interface MemberService {

    List<Member> findAll();

    Member findOne(String id) throws IOException;

    Member save(Member member);

    void update(Member member);

    void delete(String id);


}

