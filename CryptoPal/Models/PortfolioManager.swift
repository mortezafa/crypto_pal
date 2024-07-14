// PortfolioManager.swift

import Foundation
import Observation

struct PortfolioData : Codable {
    let timestamp: Date
    let balance: Double
}


@Observable class PortfolioManager {
    static let shared = PortfolioManager()

    private let portfolioDataKey = "portfolioData"

    private init() {}

    func saveCurrentBalance(_ balance: Double) {
        var currentData = getPortfolioData()
        let newEntry = PortfolioData(timestamp: Date(), balance: balance)
        currentData.append(newEntry)

        if let encoded = try? JSONEncoder().encode(currentData) {
            UserDefaults.standard.set(encoded, forKey: portfolioDataKey)
            print("Saved data: \(currentData)") // Debug statement
        }
    }

    func getPortfolioData() -> [PortfolioData] {
        if let savedData = UserDefaults.standard.data(forKey: portfolioDataKey),
           let decodedData = try? JSONDecoder().decode([PortfolioData].self, from: savedData) {
            print("Retrieved data: \(decodedData)") // Debug statement
            return decodedData
        }
        return []
    }

    func getLatestBalance() -> PortfolioData? {
        let latestBalance = getPortfolioData().last
        print("Latest balance: \(String(describing: latestBalance))") // Debug statement
        return latestBalance
    }

    func getBalance24HoursAgo() -> PortfolioData? {
        let data = getPortfolioData()
        guard let latestTimestamp = data.last?.timestamp else { return nil }

        let targetDate = Calendar.current.date(byAdding: .day, value: -1, to: latestTimestamp)!
        let balance24HoursAgo = data.last { $0.timestamp <= targetDate }
        print("Balance 24 hours ago: \(String(describing: balance24HoursAgo))") // Debug statement
        return balance24HoursAgo
    }
}
