package com.example.testsnslogin_server.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/testsnsloginsecret.json");
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
