// ContentView.swift

import SwiftUI
import Charts
struct HomeView: View {
    var viewModel: AssetViewModel
    var body: some View {
        VStack(spacing: 0) {
            TopBarView()
                .padding(.bottom)
            Spacer()
            PortfolioOverview(viewModel: viewModel)
            ListView(viewModel: viewModel)

        }
        .padding(20)
        .onAppear {
            viewModel.getAssets()
            Task {
                await viewModel.fetchSingleValue()
            }
        }

    }
}

struct MainView: View {

    var assetViewModel = AssetViewModel()
    private var chartViewModel = ChartViewModel()

    @State private var isTabBarHidden: Bool = false
    @State var selectedTab: Int = 100


    @ViewBuilder
    var tabViews: some View {

        NavigationStack {
            HomeView(viewModel: assetViewModel)
        }
        .tabItem {
            Image(systemName: "house")
            Text("Home")
        }
        .tag(1)

        NavigationStack {
            PortfolioView(chartViewModel: chartViewModel, portfolioViewModel: assetViewModel)
        }
        .tabItem {
            Image(systemName: "chart.pie.fill")
            Text("Portfolio")
        }
        .tag(2)


        NavigationStack {
            ChatView()
        }
        .tabItem {
            Image(systemName: "info.bubble")
            Text("Chat")
        }
        .tag(3)
    }

    var body: some View {
        TabView(selection: $selectedTab) {
            tabViews
                .tint(nil)
                .toolbarBackground(selectedTab == 0 ? .black : .white, for: .tabBar)
                .toolbarBackground(.visible, for: .tabBar)
        }
        .tint(selectedTab == 0 ? .white : .purple)

    }
}

#Preview {
    MainView()
}



struct TopBarView: View {
    var body: some View {
        HStack {
            Image(systemName: "square.grid.3x3")
                .resizable()
                .frame(width: 20, height: 20)
            Spacer()
            Text("CryptoPal")
                .font(.custom("Gilroy-ExtraBold", size: 26))
            Spacer()
            Image(systemName: "bell")
        }
    }
}


struct PortfolioOverview: View {
    var viewModel: AssetViewModel
    var chartViewModel = ChartViewModel()

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
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .shadow(color: .gray.opacity(0.3), radius: 10, x: 0, y: 0)
    }


}

struct ChartView: View {
    var chartViewModel: ChartViewModel
    var body: some View {
        Chart {
            ForEach(chartViewModel.balanceHistory.indices, id: \.self) { index in
                LineMark(
                    x: .value("Day", index),
                    y: .value("Balance", chartViewModel.balanceHistory[index])
                )
                .foregroundStyle(Color.purple)
                .interpolationMethod(.catmullRom)
            }
        }
        .chartXAxis(.hidden)
        .chartYAxis(.hidden)
        .frame(maxWidth: .infinity, maxHeight: 100)
    }
}

struct ListView: View {
    var viewModel: AssetViewModel

    var body: some View {
        VStack {
            HStack {
                Text("Watchlist")
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
            .padding(.horizontal)

            List {
                ForEach(viewModel.watchListAssets, id: \.self) { asset in
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
                    .listRowInsets(EdgeInsets()) // Remove default insets
                }
            }
            .listStyle(.plain)
        }
        .padding(.vertical)
    }
}

struct LogoView: View {
    var asset: Asset

    var body: some View {
        VStack {
            if let logoURL = asset.logosList, let url = URL(string: logoURL) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .empty:
                        ProgressView()
                    case .success(let image):
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 30, height: 30)
                    case .failure:
                        Image(systemName: "xmark.circle")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 50, height: 50)
                    @unknown default:
                        EmptyView()
                    }
                }
            } else {
                Text("No logo available")
            }
        }
        .padding()
    }
}
