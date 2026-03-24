package com.example.classroom.domain.classroom.controller;


import com.example.classroom.domain.classroom.mapper.ClassroomMapper;
import com.example.classroom.domain.classroom.response.ClassroomWithTeacher;
import com.example.classroom.domain.member.entity.Member;
import com.example.classroom.domain.member.mapper.MemberMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;



@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
    final MemberMapper memberMapper;
    final ClassroomMapper classroomMapper;

    // 홈페이지
    @GetMapping({"/","/index"})
    public String indexHanlde(@SessionAttribute(required = false) Member logonMember, Model model,
                              HttpSession session , HttpServletRequest request) {
        log.info("session id {}, request ip {}", session.getId(), request.getRemoteAddr());
//        System.out.println("sessionId : " +session.getId());
//
//            session.setAttribute("logonMember", memberMapper.selectById("shizo"));
//            return "index-logon";
        if(logonMember == null){
            return "index";
        }else {
            List<ClassroomWithTeacher> classes =
                    classroomMapper.selectByTeacherIdOrStudentId(logonMember.getId());
            model.addAttribute("classes",classes);
            model.addAttribute("classesSize",classes.size());

            return "index-logon";
        }


    }
}
