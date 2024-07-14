// ChartViewModel.swift

import Foundation
import Observation

@Observable class ChartViewModel {
    var portfolioBalance : Double = 0.0
    var balanceHistory: [Double] = []
    var percentageChange: Double = 0.0
    var assetModel = AssetViewModel()

    init() {
        Task {
            await fetchPortfolioData()
        }
    }

    func fetchPortfolioData() async {
            await assetModel.fetchSingleValue()
            if let value = assetModel.singleValue {
                portfolioBalance = value
                print(value)
                updateBalanceHistory()
                calculatePercentageChange()
                print(percentageChange)
                PortfolioManager.shared.saveCurrentBalance(value)
            }
        }


    private func updateBalanceHistory() {
            let portfolioData = PortfolioManager.shared.getPortfolioData()
            balanceHistory = portfolioData.map { $0.balance }
        }

        private func calculatePercentageChange() {
            guard let oldData = PortfolioManager.shared.getBalance24HoursAgo() else { return }
            let oldBalance = oldData.balance
            percentageChange = ((portfolioBalance - oldBalance) / oldBalance) * 100
        }

}
