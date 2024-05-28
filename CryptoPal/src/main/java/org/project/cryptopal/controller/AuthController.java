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
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirebaseAuthService firebaseAuthService;


    @PostMapping("/google")
   public ResponseEntity<String> authenticateWithGoogle(@RequestParam String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);
            String uid = decodedToken.getUid();
            firebaseAuthService.saveIfUserNotExists(uid);
            return ResponseEntity.ok("User authenticated with UID: " + uid);
        } catch (FirebaseAuthException e) {
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

        repsonse.put("message", "User saved Succesfully!");
        return ResponseEntity.ok(repsonse);
    }

    @PostMapping("/onboard")
    public  ResponseEntity<Map<String, String>> onBoardUser(@RequestBody Map<String, String> onboardDetails) {
        String walletAddress = onboardDetails.get("walletAddress");
        String email = onboardDetails.get("email");

        firebaseAuthService.addWalletAddress(email, walletAddress);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Wallet address updated successfully!");
        return ResponseEntity.ok(response);
    }


}
