package org.project.cryptopal.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.project.cryptopal.model.User;
import org.project.cryptopal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;


import java.time.LocalDateTime;

@Service
public class FirebaseAuthService {

    @Autowired
     private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    public void saveIfUserNotExists(String uId) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uId);
        User user = new User(userRecord.getDisplayName(), userRecord.getEmail(), LocalDateTime.now());
        userRepository.save(user);
    }

    public User saveUserForTesting(String email, String displayName) {
        User user = null; // TODO: do this
        if (user == null) {
            user = new User(displayName, email, LocalDateTime.now());
        }
        userRepository.save(user);
        return user;
    }

    public void addWalletAddress(String email, String walletAddress) {
            User user = findByEmail(email);
            user.setWalletAddress(walletAddress);
            userRepository.save(user);
    }
}
