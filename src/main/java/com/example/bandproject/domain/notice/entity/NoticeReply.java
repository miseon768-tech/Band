package com.example.bandproject.domain.notice.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class NoticeReply {
    int id;
    int noticeId;
    String writerId;
    String content;
    LocalDateTime createdAt;
}
