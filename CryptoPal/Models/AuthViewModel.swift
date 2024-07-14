// AuthViewModel.swift

import Foundation
import FirebaseCore
import FirebaseAuth
import CryptoKit
import AuthenticationServices
import GoogleSignIn
import FirebaseFirestore

struct AuthAPI {
    static let baseURL = "http://localhost:8080/api/auth"

    static func authenticateWithGoogle(idToken: String, completion: @escaping (Result<String, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/google") else {
            completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"])))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")

        let body = "idToken=\(idToken)"
        request.httpBody = body.data(using: .utf8)

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Network error: \(error.localizedDescription)")
                completion(.failure(error))
                return
            }

            if let httpResponse = response as? HTTPURLResponse {
                print("HTTP Response: \(httpResponse.statusCode)")
            } else {
                print("Invalid response: \(String(describing: response))")
            }

            guard let data = data, let response = response as? HTTPURLResponse, response.statusCode == 200 else {
                let responseString = String(data: data ?? Data(), encoding: .utf8) ?? "No data"
                print("Error: Invalid response or data: \(responseString)")
                completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid response or data"])))
                return
            }

            if let responseString = String(data: data, encoding: .utf8) {
                completion(.success(responseString))
            } else {
                completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to decode response"])))
            }
        }.resume()
    }
}


@Observable class AuthViewModel: NSObject, ASAuthorizationControllerDelegate {
    var currentUser: FirebaseAuth.User?
    var user: GIDGoogleUser?
    var isSignedIn: Bool = false

    func googleSignIn() {
        guard let clientID = FirebaseApp.app()?.options.clientID else { return }
        print("THIS IS THE CLIENT ID: \(clientID)")

        // Create Google Sign In configuration object.
        let config = GIDConfiguration(clientID: clientID)

        // As you’re not using view controllers to retrieve the presentingViewController, access it through
        // the shared instance of the UIApplication
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene else { return }
        guard let rootViewController = windowScene.windows.first?.rootViewController else { return }

        // Start the sign in flow!
        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { [unowned self] result, error in
            if let error = error {
                print("Error doing Google Sign-In, \(error)")
                return
            }

            guard
                let user = result?.user,
                let idToken = user.idToken?.tokenString
            else {
                print("Error during Google Sign-In authentication, \(String(describing: error))")
                return
            }

            self.user = user
            print("THIS IS THE ID TOKEN: \(idToken)")
            self.isSignedIn = true
            self.sendTokenToBackend(user: user)

            let credential = GoogleAuthProvider.credential(withIDToken: idToken,
                                                           accessToken: user.accessToken.tokenString)
            
            // Authenticate with Firebase
            Auth.auth().signIn(with: credential) { authResult, error in
                if let e = error {
                    print(e.localizedDescription)
                }

                print("Signed in with Google")
            }
        }
    }

    func signOut() {
        GIDSignIn.sharedInstance.signOut()
        user = nil
        isSignedIn = false
    }

    func restorePreviousSignIn() {
        GIDSignIn.sharedInstance.restorePreviousSignIn { [weak self] user, error in
            if let error = error {
                print("Error restoring previous sign-in: \(error.localizedDescription)")
            } else if let user = user {
                self?.user = user
                self?.isSignedIn = true
            }
        }
    }

    private func sendTokenToBackend(user: GIDGoogleUser) {
        guard let idToken = user.idToken?.tokenString else { return }
        AuthAPI.authenticateWithGoogle(idToken: idToken) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let response):
                    print("Authentication successful: \(response)")
                case .failure(let error):
                    print("Authentication failed: \(error.localizedDescription)")
                }
            }
        }
    }


    func updateOnboardingComplete() async throws {
        do {
            let userRef = Firestore.firestore().collection("users").document(currentUser!.uid)
            try await userRef.updateData(["onboardingComplete": true])
        } catch {
            print("DEBUG: Error updating user onboard status: \(error.localizedDescription)")
        }
    }

}
