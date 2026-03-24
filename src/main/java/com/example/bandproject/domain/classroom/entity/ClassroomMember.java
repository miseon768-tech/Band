package com.example.bandproject.domain.classroom.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassroomMember {
    int id;
    String classroomId;
    String studentId;
    boolean blocked;
}
