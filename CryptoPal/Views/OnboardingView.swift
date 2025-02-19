import SwiftUI

struct OnboardingStep {
    let question: String
    let options: [String]
    let maxSelections: Int
}

// MARK: Onboarding Questions
let onboardingSteps = [
    OnboardingStep(question: "Which description best matches your style preference?",
                   options: ["Outdoor", "Classic", "Minimalist", "Others"],
                   maxSelections: 1),

    OnboardingStep(question: "What are your main goals for sustainable consumption?",
                   options: ["Reduce carbon footprint", "Support ethical labour practices", "Reduce waste"],
                    maxSelections: 3),

    OnboardingStep(question: "How important are the following factors when choosing a sustainable fashion brand?",
                   options: ["Transparency", "Certifications (e.g., Fair Trade, B Corp)", "Local Production", "Vegan Products", "Packaging"],
                     maxSelections: 5)
]

struct OnboardingView: View {
    @State private var currentStep: Int = 0
    @State private var selectedOptions = [[String]](repeating: [], count: onboardingSteps.count)
    let onboardingStepsCount = onboardingSteps.count
    @Environment(\.presentationMode) var presentationMode
    var viewModel: AuthViewModel


    var body: some View {
            VStack (spacing: 0) {
                TabView(selection: $currentStep ) {
                    ForEach(0..<onboardingStepsCount, id: \.self) { index in
                        VStack {
                            Text(onboardingSteps[index].question)
                                .font(.title2)
                                .fontWeight(.semibold)
                                .foregroundColor(.purple)
                                .padding(.top, 55)
                                .padding(.bottom, 40)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.horizontal, 20)

                            ForEach(onboardingSteps[index].options, id: \.self) { option in
                                OptionRow(
                                    option: option,
                                    isSelected: selectedOptions[index].contains(option),
                                    toggleSelection: {
                                        // Let the user select up to `maxSelections` options
                                        // If the user has already selected `maxSelections` options, remove the first option

                                        if selectedOptions[index].contains(option) {
                                            selectedOptions[index].removeAll(where: { $0 == option })
                                        } else if selectedOptions[index].count < onboardingSteps[index].maxSelections {
                                            selectedOptions[index].append(option)
                                        } else {
                                            selectedOptions[index].removeFirst()
                                            selectedOptions[index].append(option)
                                        }
                                    }
                                )
                                .padding(.horizontal, 35)
                            }
                            Spacer()
                        }
                        .tag(index)
                    }
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                .animation(.default, value: currentStep)
                .onChange(of: currentStep) { newValue in
                    if newValue > 0 && selectedOptions[newValue - 1].isEmpty {
                        currentStep -= 1
                    }
                }

                Spacer()

                HStack (spacing: 0) {
                    Button(action: previousStep) {
                        Image(systemName: "chevron.left")
                            .font(.title)
                    }
                    .buttonStyle(.borderedProminent)

                    Spacer()


                    HStack {
                        ForEach(0..<onboardingStepsCount, id: \.self) { count in
                            if count == currentStep {
                                Rectangle()
                                    .frame(width: 20, height: 10)
                                    .cornerRadius(10)
                                    .foregroundColor(Color.blue)
                            } else {
                                Circle()
                                    .frame(width: 10, height: 10)
                                    .foregroundColor(Color.yellow.opacity(0.4))
                            }
                        }
                    }
                    .transition(.slide) // This applies the transition to the entire
                    .animation(.easeInOut, value: currentStep)

                    Spacer()

                    Button(action: isLastStep() ? finishOnboarding : nextStep) {
                        if isLastStep() {
                            HStack {
                                Text("Let's Start")
                                    .font(.title2)

                                Image(systemName: "chevron.right")
                                    .font(.body)
                            }
                        } else {
                            Image(systemName: "chevron.right")
                                .font(.title)
                        }
                    }
                    .buttonStyle(.configurable(isLastStep(), isEnabled: isCurrentStepValid()))
                    .disabled(!isCurrentStepValid())
                }
                .padding(.bottom, 25)
            }
    }

    func isLastStep() -> Bool {
        currentStep == onboardingStepsCount - 1
    }

    func previousStep() {
        withAnimation {
            if currentStep > 0 {
                currentStep -= 1
            } else {
                // Navigate to the previous screen
                // Close the presentation
                presentationMode.wrappedValue.dismiss()
            }
        }
    }

    func nextStep() {
        withAnimation {
            if currentStep < onboardingStepsCount - 1 {
                currentStep += 1
            }
        }
    }


    func isCurrentStepValid() -> Bool {
        !selectedOptions[currentStep].isEmpty
    }

    func finishOnboarding() {
        // MARK: Backend Processing
        Task {
            try await viewModel.updateOnboardingComplete()
        }
    }

}

struct OptionRow: View {
    var option: String
    var isSelected: Bool
    var toggleSelection: () -> Void

    var body: some View {
        HStack {
            Image(systemName: isSelected ? "checkmark.square.fill" : "square")
                .resizable()
                .frame(width: 22, height: 22)
                .foregroundColor(.purple)
                .onTapGesture(perform: toggleSelection)

            Text(option)
                .foregroundColor(Color.black)
                .padding(.leading)
                .onTapGesture(perform: toggleSelection)

            Spacer()
        }
        .padding(.vertical, 15)
    }
}

#if DEBUG
struct OnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        OnboardingView(viewModel: AuthViewModel())
    }
}
#endif




