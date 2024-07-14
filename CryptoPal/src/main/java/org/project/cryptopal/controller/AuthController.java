package org.project.cryptopal.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.project.cryptopal.model.User;
import org.project.cryptopal.service.FirebaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;


    @PostMapping("/google")
    public ResponseEntity<String> authenticateWithGoogle(@RequestParam String idToken) {
        try {
            System.out.println("Received ID Token: " + idToken);
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);
            String uid = decodedToken.getUid();
            System.out.println("Decoded Token UID: " + uid);
            System.out.println("Decoded Token Audience: " + decodedToken.getIssuer());
            firebaseAuthService.saveIfUserNotExists(uid);
            return ResponseEntity.ok("User authenticated with UID: " + uid);
        } catch (FirebaseAuthException e) {
            System.err.println("FirebaseAuthException: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/test-user")
    public ResponseEntity<Map<String, String>> testSaveUser(@RequestBody Map<String, String> userDetails) {
        System.out.println("in contorller code");
        String email = userDetails.get("email");
        String displayName = userDetails.get("displayName");

        User user = firebaseAuthService.saveUserForTesting(email, displayName);
        Map<String, String> repsonse = new HashMap<>();

        repsonse.put("id", user.getId().toString());
        repsonse.put("email", user.getEmail());

        return ResponseEntity.ok(repsonse);
    }

    @PostMapping("/onboard")
    public ResponseEntity<Map<String, String>> onBoardUser(@RequestBody Map<String, Object> onboardDetails) {
        List<String> walletAddress = (List<String>) onboardDetails.get("walletAddress");
        Long userId = Long.valueOf((Integer) onboardDetails.get("id"));
        List<String> walletNickname = (List<String>) onboardDetails.get("walletNickname");

        firebaseAuthService.addWalletAddress(userId, walletAddress, walletNickname);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Wallet address updated successfully!");
        return ResponseEntity.ok(response);
    }


}
