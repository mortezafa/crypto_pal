// ContentView.swift

import SwiftUI

struct ContentView: View {
    var viewModel: AuthViewModel
    var body: some View {

//if viewModel.currentUser?.onboardingComplete == false {
//            WelcomeView()
//        } else if viewModel.userSession != nil {
//            HomeView().environmentObject(self.viewModel)
//        } else {
//            LoginView()
//        }

        MainView()
    }
}
