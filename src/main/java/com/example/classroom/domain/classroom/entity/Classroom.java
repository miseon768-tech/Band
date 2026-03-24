package com.example.classroom.domain.classroom.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Classroom {
    String id;
    String teacherId;
    String name;
    String description;
    String joinCode;
    String theme;
}
