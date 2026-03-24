package com.example.classroom.domain.notice.response;

import com.example.classroom.domain.notice.entity.NoticeAttachment;
import com.example.classroom.domain.notice.entity.NoticeReply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
public class NoticeWithAttachment {
    int id;
    String classroomId;
    String content;
    LocalDateTime createdAt;
    List<NoticeAttachment> attachments;
    List<NoticeReply> replys;
}
