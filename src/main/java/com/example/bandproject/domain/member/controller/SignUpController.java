package com.example.bandproject.domain.member.controller;

import com.example.bandproject.domain.member.entity.Member;
import com.example.bandproject.domain.member.entity.Verification;
import com.example.bandproject.domain.member.mapper.MemberMapper;
import com.example.bandproject.domain.member.mapper.VerificationMapper;
import com.example.bandproject.domain.member.request.AccountCreationRequest;
import com.example.bandproject.domain.member.request.ProfileSetupRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    final MemberMapper memberMapper;
    final VerificationMapper verificationMapper;
    final JavaMailSender mailSender;

    // 회원가입 - 계정 생성
    @GetMapping("/signup/account")
    public String signupAccountHandle(HttpSession session) {
        return "signup/account";
    }

    // 회원가입 - 계정 생성 처리
    @PostMapping("/signup/account")
    public String signupAccountPostHandle(@ModelAttribute AccountCreationRequest acr, HttpSession session) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member temporalMember = new Member();
        temporalMember.setId(acr.id());

        temporalMember.setPassword(  passwordEncoder.encode(acr.password())  );
        temporalMember.setEmail(acr.email());
        session.setAttribute("temporalMember", temporalMember);
        return "redirect:/signup/profile";
    }

    // 회원가입 - 프로필 설정
    @GetMapping("/signup/profile")
    public String signupProfileHandle(HttpSession session) {
        return "signup/profile";
    }

    // 회원가입 - 프로필 설정 처리
    @PostMapping("/signup/profile")
    @Transactional
    public String signupProfilePostHandle(@ModelAttribute ProfileSetupRequest psr,
                                          @SessionAttribute Member temporalMember,
                                          HttpSession session) {
        // Member temporalMember = (Member)session.getAttribute("temporalMember");
        temporalMember.setName(psr.name());
        temporalMember.setGender(psr.gender());
        temporalMember.setBirthday(LocalDate.of(psr.year(), psr.month(), psr.day()));

        memberMapper.insertOne(temporalMember);
        // 인증코드 6 생성해서 DB 저장 및 메일 발송
        int rand = (int) (Math.random() * 1000000);
        String code = String.format("%06d", rand);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(temporalMember.getEmail());
        mailMessage.setSubject("[클래스룸] 자격 증명 확인");
        mailMessage.setText("안녕하세요. " + temporalMember.getName() + "님!\n\n" +
                "아래 인증 코드를 입력해 로그인 절차를 완료해주세요.\n\n" +
                "인증코드 : " + code +"\n\n" +
                "감사합니다."
        );
        mailSender.send(mailMessage);

        Verification verification = new Verification();
        verification.setCode(code);
        verification.setMemberId(temporalMember.getId());
        verification.setExpiredAt(LocalDateTime.now().plusDays(7));
        verificationMapper.insertOne(verification);

        session.setAttribute("logonId",temporalMember.getId());
        session.setAttribute("logonMember",temporalMember);

        return "redirect:/account/verify";
    }

}
