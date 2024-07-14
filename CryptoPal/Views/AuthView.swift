// AuthView.swift

import SwiftUI
import GoogleSignInSwift


struct AuthView: View {
    private var viewModel = AuthViewModel()
    private var googleModel = GoogleSignInButtonViewModel(scheme: .light, style: .wide, state: .normal)
    var body: some View {
        VStack {
                    if viewModel.isSignedIn {
                        HomeView(viewModel: AssetViewModel())
                    } else {
                        LoginView()
                    }
                }
                .onAppear {
                    viewModel.restorePreviousSignIn()
                }
                .padding()
            }
    }

#Preview {
    AuthView()
}
