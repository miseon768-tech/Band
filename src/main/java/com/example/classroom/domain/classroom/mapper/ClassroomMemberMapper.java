package com.example.classroom.domain.classroom.mapper;

import com.example.classroom.domain.classroom.entity.ClassroomMember;
import com.example.classroom.domain.classroom.entity.DailyCheck;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClassroomMemberMapper {

    int insertOne(ClassroomMember classroomMember);
    ClassroomMember selectByClassroomIdAndStudentId(Map<String,Object> map);
    List<String> selectStudentIdByClassroomId(String classroomId);


    int insertDailyCheck(DailyCheck dailyCheck);
    boolean existTodayDailyCheckByClassroomMemberId(int classroomMemberId);
}
