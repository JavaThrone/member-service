package com.service.member.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.service.member.model.File;
import com.service.member.model.Member;
import com.service.member.repository.MemberRepository;
import com.service.member.service.MemberService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final GridFsOperations gridFsOperations;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, GridFsOperations gridFsOperations) {
        this.memberRepository = memberRepository;
        this.gridFsOperations = gridFsOperations;
    }

    @Override
    public List<Member> findAll() {

        List<Member> members = memberRepository.findAll();

        members.stream().peek(m -> {
            GridFSFile file = gridFsOperations.findOne(new Query(Criteria.where("metadata.memberId").is(m.getId())));
            if (file != null) {
                GridFsResource resource = gridFsOperations.getResource(file);
                try {
                    m.setFile(new File(resource.getFilename(), resource.getContentType(), IOUtils.toByteArray(resource.getInputStream())));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });


        return members;
    }

    @Override
    public Member findOne(String id) throws IOException {
        Member member = memberRepository.findById(id).orElse(null);

        GridFSFile file = gridFsOperations.findOne(new Query(Criteria.where("metadata.memberId").is(id)));
        if (file != null) {
            GridFsResource resource = gridFsOperations.getResource(file);
            member.setFile(new File(resource.getFilename(), resource.getContentType(), IOUtils.toByteArray(resource.getInputStream())));
        }

        return member;
    }

    @Override
    public Member save(Member member) {

        Member savedMember = memberRepository.save(member);
        if (member.getFile() != null && member.getFile().getName() != null && member.getFile().getContentType() != null && member.getFile().getStream() != null) {
            addFile(savedMember);
        }
        return savedMember;
    }

    @Override
    public void update(Member member) {
        Member foundMember = memberRepository.findById(member.getId()).orElse(null);
        if (member.getFile() != null && member.getFile().getName() != null && member.getFile().getContentType() != null && member.getFile().getStream() != null) {
            gridFsOperations.delete(new Query(Criteria.where("metadata.memberId").is(foundMember.getId())));
            addFile(member);
        }
        memberRepository.save(member);
    }

    @Override
    public void delete(String id) {
        memberRepository.deleteById(id);
        gridFsOperations.delete(new Query(Criteria.where("metadata.memberId").is(id)));
    }

    private void addFile(Member member) {
        DBObject metaData = new BasicDBObject();
        metaData.put("memberId", member.getId());
        gridFsOperations.store(new ByteArrayInputStream(member.getFile().getStream()), member.getFile().getName(), member.getFile().getContentType(), metaData);
    }


}
