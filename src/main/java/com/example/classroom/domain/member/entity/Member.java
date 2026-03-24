package com.example.classroom.domain.member.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Member {
    String id;
    String password;
    String email;
    String name;
    String image;
    LocalDate birthday;
    String gender;
    boolean active;
}
