// PortfolioView.swift

import SwiftUI

struct PortfolioView: View {
    @State var chartViewModel: ChartViewModel
    @State var portfolioViewModel: AssetViewModel
    var body: some View {
        VStack {
            PortfolioOverviewForMainPortfolio(viewModel: portfolioViewModel, chartViewModel: chartViewModel)
                .padding()
            PortfolioListView(portfolioViewModel: portfolioViewModel)
                .padding(20 )
        }
        .onAppear {
            portfolioViewModel.getAssets()
            Task {
                await portfolioViewModel.fetchSingleValue()
            }
        }
        
    }
}

#Preview {
    PortfolioView(chartViewModel: ChartViewModel(), portfolioViewModel: AssetViewModel())
}

struct PortfolioOverviewForMainPortfolio: View {
    var viewModel: AssetViewModel
    var chartViewModel = ChartViewModel()
    var options = ["1H", "1D", "1W", "1M", "1Y", "All"]
    @State private var selectedOption: String?


    var body: some View {
        VStack(alignment: .leading) {
            Text("Portfolio balance")
                .font(Font.custom("Gilroy-ExtraBold", size: 16))
            Text("$\(String(format: "%.2f", chartViewModel.portfolioBalance))")
                .font(Font.custom("Gilroy-ExtraBold", size: 34))
                .padding(.vertical, 2)
            HStack {
                if chartViewModel.percentageChange >= 0 {
                    if chartViewModel.percentageChange == 0 {
                        Text("\(String(format: "%.2f", chartViewModel.percentageChange))% in the past day")
                            .foregroundColor(.gray)
                    } else {
                        Text("+\(String(format: "%.2f", chartViewModel.percentageChange))%")
                            .foregroundColor(.green)
                        Text("in the past day")
                    }
                } else {
                    Text("\(String(format: "%.2f", chartViewModel.percentageChange))%")
                        .foregroundColor(.red)
                    Text("in the past day")
                }
            }
            .padding(.bottom,20)
            .font(Font.custom("Gilroy-Light", size: 15))

            ChartView(chartViewModel: chartViewModel)

            HStack {
                ForEach(options, id: \.self) { option in
                    Button(action: {
                        selectedOption = option

                    }, label: {
                        Text(option)
                            .font(Font.custom("Gilroy-Light", size: 14))
                            .foregroundStyle(.black)
                    })
                    .buttonStyle(.borderedProminent)
                    .tint(selectedOption == option ? Color.purple.opacity(0.3) : Color.clear)
                    .clipShape(Capsule())

                    if option != options.last {
                        Spacer()
                    }

                }
            }
        }
        .padding()
        .background(Color.white)
    }
}

struct PortfolioListView: View {
    var portfolioViewModel: AssetViewModel

    var body: some View {
        VStack {
            HStack {
                Text("Wallet Holdings")
                    .font(Font.custom("Gilroy-ExtraBold", size: 20))
                Spacer()
                HStack {
                    Text("24 Hours")
                        .font(Font.custom("Gilroy-Light", size: 13))
                }
                .padding(10)
                .overlay(
                    Capsule(style: .continuous)
                        .stroke(Color.gray, lineWidth: 0.5)
                )
            }

                List {
                    ForEach(portfolioViewModel.allAssets.keys.sorted(), id: \.self) { address in
                        Section(header:
                                    Text(address)
                            .font(.custom("Gilroy-ExtraBold", size: 20))
                            .foregroundStyle(.black)
                        ) {
                            ForEach(portfolioViewModel.allAssets[address] ?? []){ asset in
                                HStack {
                                    LogoView(asset: asset)
                                    VStack(alignment: .leading) {
                                        Text(asset.tokenName)
                                            .font(.custom("Gilroy-Light", size: 17))
                                        Text(asset.tokenSymbol)
                                            .font(.custom("Gilroy-Light", size: 12))
                                            .padding(.top, 1)
                                    }
                                    Spacer()
                                    HStack {
                                        Text("$\(String(format: "%.2f", asset.walletPrice))")
                                    }
                                }
                                .listRowInsets(EdgeInsets())
                            }
                        }
                    }
                }
                .listStyle(.plain)
                .scrollIndicators(.hidden)
        }
    }
}
