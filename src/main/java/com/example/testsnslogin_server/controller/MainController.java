package com.example.testsnslogin_server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class MainController {

    @Autowired
    KakaoService kakaoService;
    

    @RequestMapping("test")
    public String testConnect() {

        return "연결성공";
    }

    @RequestMapping("kakao/sign_in")
    public ModelAndView kakaoSignIn(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) {
        System.out.println("=========================");

        kakaoService.execKakaoLogin(code);


        return null;
    }

    public static JsonNode getAccessToken(String autorize_code) { 
        return null; 
    }

}
