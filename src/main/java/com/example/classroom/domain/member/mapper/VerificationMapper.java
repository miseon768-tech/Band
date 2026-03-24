package com.example.classroom.domain.member.mapper;

import com.example.classroom.domain.member.entity.Verification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationMapper {
    int insertOne(Verification one);
    Verification selectLatestByMemberId(String memberId);
}
