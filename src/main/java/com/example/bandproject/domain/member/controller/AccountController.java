package com.example.bandproject.domain.member.controller;

import com.example.bandproject.domain.member.entity.Member;
import com.example.bandproject.domain.member.entity.Verification;
import com.example.bandproject.domain.member.mapper.MemberMapper;
import com.example.bandproject.domain.member.mapper.VerificationMapper;
import com.example.bandproject.domain.member.request.VerifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController {
    final VerificationMapper verificationMapper;
    final MemberMapper memberMapper;

    // 인증코드 입력 폼
    @GetMapping("/account/verify")
    public String verifyHandle(@SessionAttribute(required = false) Member logonMember,
                               Model model) {
        if(logonMember == null){
            return "redirect:/login";
        }

        model.addAttribute("logonId", logonMember.getId());

        return "account/verify";
    }

    /*
        인증받을 사용자 계정아이디랑 인증코드
     */
    @PostMapping("/account/verify")
    public String verifyPostHandle(@ModelAttribute VerifyRequest verifyRequest, RedirectAttributes ra) {
        Verification verification = verificationMapper.selectLatestByMemberId(verifyRequest.memberId());
        if (verification == null) {
            ra.addFlashAttribute("error", "발급된 인증코드가 없습니다");
            return "redirect:/account/verify";
        }
        if (!verification.getCode().equals(verifyRequest.code()) || verification.getExpiredAt().isBefore(LocalDateTime.now())) {
            ra.addFlashAttribute("error", "인증코드가 일치하지 않거나 유요하지 유효하지 않습니다. ");
            return "redirect:/account/verify";
        }
        memberMapper.updateActive(verifyRequest.memberId());
        return "redirect:/index";
    }
}
