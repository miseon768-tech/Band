package com.example.bandproject.domain.classroom.controller;


import com.example.bandproject.domain.classroom.entity.Classroom;
import com.example.bandproject.domain.classroom.entity.ClassroomMember;
import com.example.bandproject.domain.classroom.mapper.ClassroomMapper;
import com.example.bandproject.domain.classroom.mapper.ClassroomMemberMapper;
import com.example.bandproject.domain.classroom.request.CreateClassroomRequest;
import com.example.bandproject.domain.member.entity.Member;
import com.example.bandproject.domain.member.mapper.MemberMapper;
import com.example.bandproject.domain.notice.entity.Notice;
import com.example.bandproject.domain.notice.entity.NoticeAttachment;
import com.example.bandproject.domain.notice.entity.NoticeReply;
import com.example.bandproject.domain.notice.mapper.NoticeMapper;
import com.example.bandproject.domain.notice.request.CreateNoticeRequest;
import com.example.bandproject.domain.notice.request.CreateReplyRequest;
import com.example.bandproject.domain.notice.response.NoticeWithAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/classroom")
public class ClassroomController {
    final ClassroomMapper classroomMapper;
    final ClassroomMemberMapper classroomMemberMapper;
    final NoticeMapper noticeMapper;
    final MemberMapper memberMapper;


    // 클래스룸 생성 페이지
    @GetMapping
    public String classroomHandle(){
        return "classroom/form";
    }

    // 클래스룸 생성 처리
    @PostMapping
    public String classroomPostHandle(@ModelAttribute CreateClassroomRequest ccr,
                                      @SessionAttribute Member logonMember){
        Classroom classroom = new Classroom();
        classroom.setName(ccr.name());
        classroom.setDescription(ccr.description());
        classroom.setTeacherId(logonMember.getId());
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        classroom.setId(id);
        String joinCode = UUID.randomUUID().toString().split("-")[0];
        classroom.setJoinCode(joinCode);
        classroomMapper.insertOne(classroom);

        return "redirect:/classroom/"+id;
    }

    // 클래스룸 메인 페이지
    @GetMapping("/{classroomId}")
    public String classroomMainHandle(@PathVariable String classroomId,
                                      @SessionAttribute String logonId,
                                      Model model){
        Classroom found =classroomMapper.selectById(classroomId);
        if(found == null){
            return "redirect:/index";
        }
        List<Notice> notices = noticeMapper.selectNoticesByClassroomId(classroomId);
        List<NoticeWithAttachment> noticeWithAttachments = new ArrayList<>();
        for(Notice one : notices) {
            NoticeWithAttachment nwa = new NoticeWithAttachment();
            nwa.setContent(one.getContent());
            nwa.setCreatedAt(one.getCreatedAt());
            nwa.setId(one.getId());
            nwa.setClassroomId(one.getClassroomId());
            nwa.setAttachments(noticeMapper.selectAttachmentsByNoticeId(one.getId()));
            nwa.setReplys(noticeMapper.selectReplysByNoticeId(one.getId()));
            noticeWithAttachments.add(nwa);
        }

        Map<String, Object> criteria = Map.of("classroomId", classroomId, "studentId", logonId);

        if(!logonId.equals(found.getTeacherId())) {
            ClassroomMember member = classroomMemberMapper.selectByClassroomIdAndStudentId(criteria);
            boolean exist = classroomMemberMapper.existTodayDailyCheckByClassroomMemberId(member.getId());
            model.addAttribute("exist", exist);
        }

        model.addAttribute("notices", noticeWithAttachments);

        model.addAttribute("classroom",found);

        String mode = found.getTeacherId().equals(logonId) ? "TEACHER" : "STUDENT";
        model.addAttribute("mode", mode);

        return "classroom/main";
    }

    // 출석부 페이지
    @GetMapping("/{classroomId}/attendance")
    public String classroomAttendanceHandle(@PathVariable String classroomId, Model model){
        // 클래스 룸 정보
        Classroom found =classroomMapper.selectById(classroomId);
        if(found == null){
            return "redirect:/index";
        }
        model.addAttribute("classroom", found);
        // 교사 정보
        model.addAttribute("teacher",
                memberMapper.selectById(found.getTeacherId()));
        // 학생 정보
        List<String> studentIds = classroomMemberMapper.selectStudentIdByClassroomId(classroomId);

        if(!studentIds.isEmpty()) {
            List<Member> students = memberMapper.selectByIds(studentIds);
            model.addAttribute("students", students);
        }else {
            model.addAttribute("students", new ArrayList<>());
        }

        return "classroom/attendance";
    }




    // 공지사항 등록 처리
    @PostMapping("/{classroomId}/notice")
    public String noticePostHandle(@PathVariable String classroomId,
                                   @ModelAttribute CreateNoticeRequest cnr) throws InterruptedException, IOException {
        // 요청자 이 클래스룸의 teacher 가 맞는지 도 백에서 검증해서 등록되게 유도

        // 노티스 DB저장해주고
        if(cnr.content().equals("") && cnr.attachment().getFirst().isEmpty()){

        }

        Notice notice = new Notice();
        notice.setClassroomId(classroomId);
        notice.setContent(cnr.content());
        noticeMapper.insertNotice(notice);
        // 업로드 처리해주고
        for(MultipartFile file : cnr.attachment()) {
            if(file.isEmpty()){
                continue;
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            Path uplodadPath = Path.of(System.getProperty("user.home"), "files", uuid);
            Files.createDirectories(uplodadPath);
            file.transferTo(Path.of(uplodadPath.toString(), file.getOriginalFilename()));
            // 업로드 된 파일들 정보까지 DB저장
            NoticeAttachment attachment = new NoticeAttachment();
            attachment.setNoticeId(notice.getId());
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileSize(file.getSize());
            attachment.setFileContentType(file.getContentType());
            attachment.setFileUrl("/files/"+uuid+"/"+file.getOriginalFilename());
            noticeMapper.insertAttachment(attachment);
        }
        return "redirect:/classroom/"+classroomId;
    }

    // 댓글 등록 처리
    @PostMapping("/{classroomId}/reply")
    public String replyHandle(@PathVariable String classroomId,
                              @ModelAttribute CreateReplyRequest crr,
                              @SessionAttribute String logonId) {
        // 요청자 이 클래스룸의 teacher 가 맞는지 도 백에서 검증해서 등록되게 유도
        NoticeReply noticeReply = new NoticeReply();
        noticeReply.setNoticeId(crr.getNoticeId());
        noticeReply.setContent(crr.getContent());
        noticeReply.setWriterId(logonId);

        noticeMapper.insertReply(noticeReply);

        return "redirect:/classroom/"+classroomId;
    }






// Join code로 클래스룸 참여 처리
    @PostMapping("/join")
    public String classroomJoinHandle(@RequestParam String joinCode, @SessionAttribute String logonId) {
        Classroom classroom =
                classroomMapper.selectByJoinCode(joinCode);
        if(classroom == null){
            return "redirect:/index";
        }
        if(logonId.equals(classroom.getTeacherId())) {
            return "redirect:/index";
        }
        ClassroomMember classroomMember = classroomMemberMapper.selectByClassroomIdAndStudentId(
                Map.of("classroomId", classroom.getId(), "studentId", logonId)
        );
        if(classroomMember == null){
            ClassroomMember newMember = new ClassroomMember();
            newMember.setClassroomId(classroom.getId());
            newMember.setStudentId(logonId);
            classroomMemberMapper.insertOne(newMember);
        }
        return "redirect:/classroom/"+classroom.getId();
    }




}
