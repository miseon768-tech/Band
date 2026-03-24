package com.example.classroom.domain.member.controller;


import com.example.classroom.domain.member.entity.Member;
import com.example.classroom.domain.member.mapper.MemberMapper;
import com.example.classroom.domain.member.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {
    final MemberMapper memberMapper;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginHandle() {

        return "login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginPostHandle(@ModelAttribute LoginRequest loginRequest,
                                  RedirectAttributes ra,
                                  HttpSession session) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Member found = memberMapper.selectById(loginRequest.id());
        if(found == null || !passwordEncoder.matches(loginRequest.password(), found.getPassword())  ) {
            ra.addFlashAttribute("error", "fail");
            return "redirect:/login";
        }
        session.setAttribute("logonId", found.getId());
        session.setAttribute("logonMember", found);
        if(!found.isActive()) {
            return "redirect:/account/verify";
        }

        return "redirect:/index";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logoutHandle(HttpSession session) {
         session.invalidate();
//        session.removeAttribute("logonId");
//        session.removeAttribute("logonMember");
        return "redirect:/index";
    }
}
