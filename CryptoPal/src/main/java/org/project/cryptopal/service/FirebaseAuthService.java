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

    public void addWalletAddress(Long id, List<String> walletAddress, List<String> walletNickname) {
        if (walletAddress.size() != walletNickname.size()) {
            throw new IllegalArgumentException("The number of wallet addresses must match the number of wallet nicknames");
        }

        //find the user once outside the loop
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        for (int i = 0; i < walletAddress.size(); i++) {
            //instaciate wallet object???
            WalletAddress newWalletAddress = new WalletAddress();
            newWalletAddress.setWalletAddress(walletAddress.get(i));
            newWalletAddress.setWalletNickname(walletNickname.get(i));
            newWalletAddress.setUser(user);

            //save to wallet repository
            walletRepository.save(newWalletAddress);
        }
    }


    public List<WalletAddress> getWalletAddressesById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getWalletAddress();
        }
        throw new RuntimeException("Could not fetch user by ID: " + id);
    }
}
