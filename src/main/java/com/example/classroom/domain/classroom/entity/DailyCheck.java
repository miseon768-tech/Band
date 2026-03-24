package com.example.classroom.domain.classroom.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class DailyCheck {
    int id;
    int classroomMemberId;
    LocalDate checkedAt;
}
