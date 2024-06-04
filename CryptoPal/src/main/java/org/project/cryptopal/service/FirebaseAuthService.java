package org.project.cryptopal.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.project.cryptopal.model.User;
import org.project.cryptopal.model.WalletAddress;
import org.project.cryptopal.repository.UserRepository;
import org.project.cryptopal.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
public class FirebaseAuthService {

    @Autowired
     private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

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
            WalletAddress walletAddress1 = walletRepository.getReferenceById()
            walletAddress1.setWalletAddress(walletAddress);
            walletRepository.findByEmail(email);
    }

    public String getWalletAddressByEmail(String email) {
        WalletAddress walletAddress = walletRepository.findByEmail(email);

    }
}
