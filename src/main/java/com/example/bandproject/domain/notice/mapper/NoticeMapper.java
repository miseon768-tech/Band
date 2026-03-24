package com.example.bandproject.domain.notice.mapper;


import com.example.bandproject.domain.notice.entity.Notice;
import com.example.bandproject.domain.notice.entity.NoticeAttachment;
import com.example.bandproject.domain.notice.entity.NoticeReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    int insertNotice(Notice notice);
    int insertAttachment(NoticeAttachment noticeAttachment);
    int insertReply(NoticeReply noticeReply);

    List<Notice> selectNoticesByClassroomId(String classroomId);
    List<NoticeAttachment> selectAttachmentsByNoticeId(int noticeId);
    List<NoticeReply> selectReplysByNoticeId(int noticeId);
}
