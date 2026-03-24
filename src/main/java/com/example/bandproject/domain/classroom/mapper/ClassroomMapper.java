package com.example.bandproject.domain.classroom.mapper;


import com.example.bandproject.domain.classroom.entity.Classroom;
import com.example.bandproject.domain.classroom.response.ClassroomWithTeacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassroomMapper {
    int insertOne(Classroom classroom);
    Classroom selectById(String id);
    Classroom selectByJoinCode(String joinCode);
    List<ClassroomWithTeacher> selectByTeacherIdOrStudentId(String memberId);
}
