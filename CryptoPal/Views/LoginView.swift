// LoginView.swift

import SwiftUI
import CustomTextField
import GoogleSignInSwift


struct LoginView: View {
    var authViewmodel = AuthViewModel()
    @State var email = ""
    @State var password = ""

    var body: some View {
        VStack {
            Image(systemName: "chart.bar.fill")
                .resizable()
                .frame(width: 100 , height: 100)
                .foregroundStyle(.purple)
            Text("Login to CryptoPal")
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
            EGTextField(text: $email)
                .setTitleText("Email")
                .setTitleFont(Font.custom("Gilroy-Light", size: 17))
                .setPlaceHolderText("Exmaple@email.com")
                .padding(.horizontal)
            EGTextField(text: $email)
                .setTitleText("Password")
                .setSecureText(true)
                .setTitleFont(Font.custom("Gilroy-Light", size: 17))
                .setPlaceHolderText("password")
                .padding()
            Text("Forgot Password? ")
                .font(Font.custom("Gilroy-Light", size: 15))
                .bold()
                .underline()
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)

            Button(action: {}, label: {
                Text("Login")
                    .frame(maxWidth: .infinity)
                    .frame(height: 35)
                    .font(Font.custom("Gilroy-ExtraBold", size: 18))
            })
            .buttonStyle(.borderedProminent)
            .padding()
            .tint(.purple)


            Text("or")
                .font(Font.custom("Gilroy-Light", size: 17))

            GoogleSignInButton {
                authViewmodel.googleSignIn()
            }
            .padding(.horizontal)

            HStack {
                Button(action: {}, label: {

                        Image(systemName: "apple.logo")
                            .resizable()
                            .frame(width: 20, height: 25)
                        Text("Continue with Apple")
                            .frame(height: 35)
                            .font(Font.custom("Gilroy-ExtraBold", size: 18))

                })
            }
            .frame(maxWidth: .infinity)
            .buttonStyle(.bordered)
            .tint(.white)
            .foregroundStyle(.black)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(Color.black, lineWidth: 1.5)
                )
            .padding()
        }

        .font(Font.custom("Gilroy-ExtraBold", size: 28))
    }
}

#Preview {
    LoginView()
}
