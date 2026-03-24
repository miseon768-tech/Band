package com.example.bandproject.domain.member.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Verification {
    int id;
    String memberId;
    String code;
    LocalDateTime expiredAt;
}












