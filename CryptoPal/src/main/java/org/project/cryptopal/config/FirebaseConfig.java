package org.project.cryptopal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("cryptopal-d8870-firebase-adminsdk-cqztf-74f6b34aeb.json");
        assert serviceAccount != null;
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("cryptopal-d8870")
                .build();
        return FirebaseApp.initializeApp(options);
    }
}
