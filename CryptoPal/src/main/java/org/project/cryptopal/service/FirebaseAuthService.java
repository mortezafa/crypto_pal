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
import java.util.*;

@Service
public class FirebaseAuthService {

    @Autowired
     private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

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

    public void addWalletAddress(String email, List<String> walletAddress) {
        for(String walletAddresses: walletAddress) {
            // find user by Id in user repo
            User user = userRepository.findByEmail(email);
            // error handling if user null
            if (user == null) {
                throw new RuntimeException("There is no user with this Email" + email);
            }

            // instanicate and set walletaddress???
            WalletAddress newWalletAddress = new WalletAddress();
            newWalletAddress.setWalletAddress(walletAddresses);
            newWalletAddress.setUser(user);


            // save to wallet repo
            walletRepository.save(newWalletAddress);
        }
    }

    public List<WalletAddress> getWalletAddressesByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User with email: " + email + " not found");
        }
        return user.getWalletAddress();
    }
}
