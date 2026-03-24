package com.example.classroom.domain.notice.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateReplyRequest {
    private int noticeId;
    private String content;
}
