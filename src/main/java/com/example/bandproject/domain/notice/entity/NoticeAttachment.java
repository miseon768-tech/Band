package com.example.bandproject.domain.notice.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeAttachment {
    int id;
    int noticeId;
    String fileName;
    long fileSize;
    String fileUrl;
    String fileContentType;
}
