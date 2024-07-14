// AssetViewModel.swift

import Foundation
import Observation




@Observable class AssetViewModel {

    var allAssets: [String: [Asset]] = [:]
    var singleValue: Double?
    var watchListAssets : [Asset] =
    [
        Asset(tokenName: "Bitcoin", tokenSymbol: "BTC", walletPrice: 84480.43, logosList: "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/800px-Bitcoin.svg.png"),

        Asset(tokenName: "Ethereum", tokenSymbol: "ETH", walletPrice: 4664.87, logosList: "https://logowik.com/content/uploads/images/ethereum3649.jpg"),

        Asset(tokenName: "POOCOin", tokenSymbol: "ABC", walletPrice: 34523, logosList: "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Bitcoin.svg/800px-Bitcoin.svg.png")

    ]



    func getAssets() {
        guard let url = URL(string: "http://localhost:8080/api/requestAssets/getAssetsPrice?id=1") else {
            print("Bad URL")
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            if let error = error {
                print("Error fetching data: \(error.localizedDescription)")
                return
            }
            guard let httpResponse = response as? HTTPURLResponse,
                  (200...299).contains(httpResponse.statusCode) else {
                print("Error with the response, unexpected status code: \(String(describing: response))")
                return
            }
            if let jsonData = data {
                print(String(data: jsonData, encoding: .utf8) ?? "Failed to print JSON data") // <-- Add this line

                DispatchQueue.main.async {
                    if let assetDictionary = self?.decodeAssets(from: jsonData) {
                        self?.allAssets = assetDictionary.assets
                        print("Here are the assets: ", self?.allAssets ?? "noo asset dict")
                    }
                }
            }
        }.resume()
    }

    private func decodeAssets(from jsonData: Data) -> AssetDictionary? {
        let decoder = JSONDecoder()
        do {
            let assetDictionary = try decoder.decode(AssetDictionary.self, from: jsonData)
            return assetDictionary
        } catch {
            print("Error decoding JSON: \(error)")
            return nil
        }
    }

    func fetchSingleValue() async {
        guard let url = URL(string: "http://localhost:8080/api/requestAssets/totalPortfolioAmount") else {
            print("Bad URL")
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            guard let httpResponse = response as? HTTPURLResponse,
                  (200...299).contains(httpResponse.statusCode) else {
                print("Error with the response, unexpected status code: \(response)")
                return
            }
            let value = decodeSingleValue(from: data)
            self.singleValue = value
        } catch {
            print("Error fetching data: \(error.localizedDescription)")
        }
    }

    private func decodeSingleValue(from jsonData: Data) -> Double? {
        let decoder = JSONDecoder()
        do {
            let value = try decoder.decode(Double.self, from: jsonData)
            return value
        } catch {
            print("Error decoding JSON: \(error)")
            return nil
        }
    }
}
