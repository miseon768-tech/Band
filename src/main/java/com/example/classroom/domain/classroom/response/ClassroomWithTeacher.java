package com.example.classroom.domain.classroom.response;

import com.example.classroom.domain.classroom.entity.Classroom;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassroomWithTeacher extends Classroom {
    boolean teacher;
}
