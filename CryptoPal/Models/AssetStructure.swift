import Foundation

import Foundation

struct Asset: Identifiable, Decodable, Hashable {
    let id = UUID()
    var tokenName: String
    var tokenSymbol: String
    var walletPrice: Double
    var logosList: String?

    enum CodingKeys: String, CodingKey {
        case tokenName, tokenSymbol, walletPrice, logos
    }

    struct Logo: Decodable {
        var uri: String
    }

    init(tokenName: String, tokenSymbol: String, walletPrice: Double, logosList: String?) {
            self.tokenName = tokenName
            self.tokenSymbol = tokenSymbol
            self.walletPrice = walletPrice
            self.logosList = logosList
        }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.tokenName = try container.decode(String.self, forKey: .tokenName)
        self.tokenSymbol = try container.decode(String.self, forKey: .tokenSymbol)
        self.walletPrice = try container.decode(Double.self, forKey: .walletPrice)

        if let logos = try? container.decode([Logo].self, forKey: .logos),
           let firstLogo = logos.first {
            self.logosList = firstLogo.uri
        }
    }
}


import Foundation

struct AssetDictionary: Decodable {
    var assets: [String: [Asset]]

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CustomCodingKeys.self)
        var tempAssets = [String: [Asset]]()

        for key in container.allKeys {
            let address = key.stringValue
            let assetArray = try container.decode([Asset].self, forKey: key)
            tempAssets[address] = assetArray
        }
        self.assets = tempAssets
    }

    struct CustomCodingKeys: CodingKey {
        var stringValue: String
        var intValue: Int?

        init?(stringValue: String) {
            self.stringValue = stringValue
        }

        init?(intValue: Int) {
            self.intValue = intValue
            self.stringValue = "\(intValue)"
        }
    }
}

