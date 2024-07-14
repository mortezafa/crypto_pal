import SwiftUI
import AVKit

struct WelcomeView: View {
    @State private var showOnboarding = false
    @State private var player = AVPlayer()
    var viewModel: AuthViewModel

    var body: some View {
        ZStack(alignment: .bottom) {
           Rectangle()
                .ignoresSafeArea()
                .padding(.bottom, 255)


            Spacer()

            VStack(alignment: .center, spacing: 40) {
                VStack (alignment: .leading, spacing: 20) {
                    Text("Hi, " + (viewModel.user?.profile?.name ?? "there") + "!")
                        .font(.system(size: 32))
                        .fontWeight(.bold)
                        .foregroundColor(.green.opacity(0.5))

                    Text("We connect you to sustainability brands and help you save money!")
                        .font(.body)
                        .foregroundColor(.red.opacity(0.5))
                        .multilineTextAlignment(.leading)
                }
                .padding(.top, 25)
                .padding(.horizontal, 30)

                Button("Get Started", action: {
                    withAnimation {
                        showOnboarding.toggle()
                    }
                    })
                    .buttonStyle(PrimaryButtonStyle(width: 300))
                    .padding(.bottom, 45)
                    .padding(.top)

            }
            .frame(maxWidth: .infinity)
            .background(Color.white)
            .cornerRadius(15)

        }
        .fullScreenCover(isPresented: $showOnboarding) {
            OnboardingView(viewModel: AuthViewModel())
        }
        .edgesIgnoringSafeArea(.all)
        .background(Color(UIColor.systemGray6))
        .navigationBarBackButtonHidden(true)
        .overlay(
            GeometryReader { geometry in
                Color.white.opacity(0.65) // Adjust the opacity here for semi-transparency
                    .frame(width: geometry.size.width, height: geometry.safeAreaInsets.top)
                    .edgesIgnoringSafeArea(.top)
            }, alignment: .top
        )
    }
}

#if DEBUG
struct WelcomeView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeView(viewModel: AuthViewModel())
    }
}
#endif

