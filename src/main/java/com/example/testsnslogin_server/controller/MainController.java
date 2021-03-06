package com.example.testsnslogin_server.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    KakaoService kakaoService;

    @RequestMapping("test")
    @ResponseBody
    public String testConnect() {
        return "연결성공";
    }

    @RequestMapping("kakao/sign_in")
    public String kakaoSignIn(@RequestParam("code") String code) {
        Map<String,Object> result =  kakaoService.execKakaoLogin(code);

        return "redirect:webauthcallback://success?customToken="+result.get("customToken").toString();
    }
}
