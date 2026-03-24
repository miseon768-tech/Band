package com.example.classroom.config;


import com.example.classroom.domain.member.entity.Member;
import com.example.classroom.domain.member.entity.Verification;
import com.example.classroom.domain.classroom.entity.ClassroomMember;
import com.example.classroom.domain.classroom.entity.DailyCheck;
import com.example.classroom.domain.classroom.mapper.ClassroomMemberMapper;
import com.example.classroom.domain.member.mapper.MemberMapper;
import com.example.classroom.domain.member.mapper.VerificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/ajax")
@RequiredArgsConstructor
public class AjaxController {
    final MemberMapper memberMapper;
    final ClassroomMemberMapper classroomMemberMapper;
    final MailSender mailSender;
    final VerificationMapper verificationMapper;

    // 회원가입 - 아이디 중복 확인
    @GetMapping("/signup/idcheck")
    @ResponseBody
    public String signupIdCheckHandle(@RequestParam String id) {
        Member found = memberMapper.selectById(id);
        if (found != null) {
            return "duplicated";
        } else {
            return "available";
        }
    }

    // 계정 인증 코드 재전송 처리
    @PostMapping("/account/verify/resend")
    @ResponseBody
    public String accountVerifyResendHandle(@SessionAttribute String logonId) {
        Member member = memberMapper.selectById(logonId);
        int rand = (int) (Math.random() * 1000000);
        String code = String.format("%06d", rand);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());
        mailMessage.setSubject("[클래스룸] 자격 증명 확인");
        mailMessage.setText("안녕하세요. " + member.getName() + "님!\n\n" +
                "아래 인증 코드를 입력해 로그인 절차를 완료해주세요.\n\n" +
                "인증코드 : " + code +"\n\n" +
                "감사합니다."
        );
        mailSender.send(mailMessage);

        Verification verification = new Verification();
        verification.setCode(code);
        verification.setMemberId(member.getId());
        verification.setExpiredAt(LocalDateTime.now().plusDays(7));
        verificationMapper.insertOne(verification);

        return "success";
    }

    // 클래스룸 출석 체크 처리
    @PostMapping("/classroom/{classroomId}/dailyCheck")
    @ResponseBody
    public String classroomDailyCheckHandle(@PathVariable String classroomId, @SessionAttribute String logonId) {
        // 이 요청을 보낸 사용자의 classroom_member 의 id 를 찾아야 함
        Map<String, Object> criteria = Map.of("classroomId", classroomId, "studentId", logonId);
        ClassroomMember member = classroomMemberMapper.selectByClassroomIdAndStudentId(criteria);
        if (member == null) {
            return "error";
        }
        int classroomMemberId = member.getId();
        // 이 사용자가 오늘 체크를 했는지 확인
        boolean r = classroomMemberMapper.existTodayDailyCheckByClassroomMemberId(classroomMemberId);
        if (r) {
            return "already_checked";
        }
        DailyCheck dailyCheck = new DailyCheck();
        dailyCheck.setClassroomMemberId(classroomMemberId);
        dailyCheck.setCheckedAt(LocalDate.now());
        classroomMemberMapper.insertDailyCheck(dailyCheck);
        return "checked";
    }


}
