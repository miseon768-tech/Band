package com.example.classroom.domain.notice.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Notice {
    int id;
    String classroomId;
    String content;
    LocalDateTime createdAt;
}
