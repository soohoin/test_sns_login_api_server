package com.example.testsnslogin_server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.stereotype.Service;

@Service
public class KakaoService {

        public Map<String,Object> execKakaoLogin(String authorize_code) {

            Map<String,Object> result = new HashMap<String,Object>();
            
            // 엑세스 토큰 받기
            String accessToken = getAccessToken(authorize_code);
            result.put("accessToken", accessToken);
            
            // 사용자 정보 읽어오기 
            Map<String,Object> userInfo = getUserInfo(accessToken);
            result.put("userInfo", userInfo);

            System.out.println(userInfo.toString());
            return result;
        }

        public String getAccessToken (String authorize_code) {
            String access_Token = "";
            String refresh_Token = "";
            String reqURL = "https://kauth.kakao.com/oauth/token";

            try {
                URL url = new URL(reqURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로

                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                StringBuilder sb = new StringBuilder();
                sb.append("grant_type=authorization_code");
                sb.append("&client_id=75be45c34d7befda1bd48e88afe5fe44");  //본인이 발급받은 key
                sb.append("&redirect_uri=http://192.168.0.6:8080/kakao/sign_in");     // 본인이 설정해 놓은 경로
                sb.append("&code=" + authorize_code);
                bw.write(sb.toString());
                bw.flush();

                // 결과 코드가 200이라면 성공
                int responseCode = conn.getResponseCode();
                System.out.println("responseCode : " + responseCode);

                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                JsonElement element = JsonParser.parseString(result);

                access_Token = element.getAsJsonObject().get("access_token").getAsString();
                refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

                System.out.println("access_token : " + access_Token);
                System.out.println("refresh_token : " + refresh_Token);

                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return access_Token;
        }



        public Map<String, Object> getUserInfo (String access_Token) {

            //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
            Map<String, Object> userInfo = new HashMap<>();
            String reqURL = "https://kapi.kakao.com/v2/user/me";
            try {
                URL url = new URL(reqURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                //    요청에 필요한 Header에 포함될 내용
                conn.setRequestProperty("Authorization", "Bearer " + access_Token);

                int responseCode = conn.getResponseCode();
                System.out.println("responseCode : " + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                JsonElement element = JsonParser.parseString(result);

                JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
                JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

                String nickname = properties.getAsJsonObject().get("nickname").getAsString();
                // String profile_image = properties.getAsJsonObject().get("profile_image").getAsString();
                String email = kakao_account.getAsJsonObject().get("email").getAsString();

                userInfo.put("nickname", nickname);
                userInfo.put("email", email);
                // userInfo.put("profile_image", profile_image);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return userInfo;
        }


        public String logout (String access_Token) {
            String reqURL = "https://kapi.kakao.com/v1/user/logout";
            String id = "";
            try {
                URL url = new URL(reqURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                //    요청에 필요한 Header에 포함될 내용
                conn.setRequestProperty("Authorization", "Bearer " + access_Token);

                // 결과 코드가 200이라면 성공
                int responseCode = conn.getResponseCode();
                System.out.println("responseCode : " + responseCode);

                // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
                // JsonElemenidt element = JsonParser.parseString(result);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return access_Token;
        }
}