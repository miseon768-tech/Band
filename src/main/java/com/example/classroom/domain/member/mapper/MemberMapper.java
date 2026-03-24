package com.example.classroom.domain.member.mapper;

import com.example.classroom.domain.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
    int insertOne(Member member);
    Member selectById(String id);
    int updateActive(String id);
    int updateImage(Map<String,Object> map);
    List<Member> selectByIds(List<String> ids);
}
