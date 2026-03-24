package com.example.classroom.domain.classroom.mapper;


import com.example.classroom.domain.classroom.entity.Classroom;
import com.example.classroom.domain.classroom.response.ClassroomWithTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassroomMapper {
    int insertOne(Classroom classroom);
    Classroom selectById(String id);
    Classroom selectByJoinCode(String joinCode);
    List<ClassroomWithTeacher> selectByTeacherIdOrStudentId(String memberId);
}
