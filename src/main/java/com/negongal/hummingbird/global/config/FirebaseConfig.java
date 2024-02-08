package com.negongal.hummingbird.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Slf4j
@Profile("!test")
@Configuration
public class FirebaseConfig {

    @Value("classpath:firebase-admin.json")
    private Resource resource;

    @PostConstruct
    public void initFirebase() throws IOException {
        log.info("Start to initializing the Firebase");
//        FileInputStream serviceAccount =
//                new FileInputStream(resource.getFile());
        InputStream inputStream = resource.getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
