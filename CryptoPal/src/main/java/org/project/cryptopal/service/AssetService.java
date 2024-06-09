package org.project.cryptopal.service;

import org.project.cryptopal.DTOs.AssetDTO;
import org.project.cryptopal.model.Asset;
import org.project.cryptopal.model.WalletAddress;
import org.project.cryptopal.repository.AssetRepository;
import org.project.cryptopal.respones.GetAssetsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssetService {
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);


    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private FirebaseAuthService authService;


    public List<Asset> fetchUserAssets(Long id) {
        List<WalletAddress> walletAddresses = authService.getWalletAddressesById(id);
        if (walletAddresses.isEmpty()) {
            throw new RuntimeException("User with email: " + id + "Has not registered any wallet yet");
        }
        ArrayList<Asset> allAssets = new ArrayList<>();
        for (WalletAddress walletAddress : walletAddresses) {
            int page = 1;
            boolean morePages = true;
            while (morePages) {
                String url = String.format("https://api.chainbase.online/v1/account/tokens?chain_id=1&address=%s&contract_address=&limit=100&page=%d", walletAddress.getWalletAddress(), page);
                HttpHeaders headers = new HttpHeaders();
                headers.set("x-api-key", "2heWvLUW25YbOqICnVPskNM4nB4");
                HttpEntity<String> entity = new HttpEntity<>(headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<GetAssetsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetAssetsResponse.class); //THIS IS WHERE PROGRAM BREAKS
                GetAssetsResponse getAssetsResponse = response.getBody();


                if (getAssetsResponse != null && getAssetsResponse.getResult() != null) {
                    for (Asset asset : getAssetsResponse.getResult()) {
                        asset.setWalletAddress(walletAddress); //setting the wallet address for each asset
                        allAssets.add(asset);
                    }
                    if (getAssetsResponse.getNextPage() != null) {
                        page = getAssetsResponse.getNextPage();
                    } else {
                        morePages = false;
                    }
                } else {
                    throw new RuntimeException("Failed to fetch from API.");
                }
            }
        }
        assetRepository.saveAll(allAssets);
        return allAssets;
    }

    public Double getTotalWalletPrice() {
        return assetRepository.getTotalPortfolioPrice();
    }

    public Map<String, List<AssetDTO>> getAssetsMoreThanZero(Long userId) {
        List<Asset> assets = assetRepository.getAllAssetsWithPriceMoreThanZero(userId);

        Map<String, List<AssetDTO>> assetsByWallet = new HashMap<>();
        for (Asset asset : assets) {
            // TODO: fix confusion here... first get method gets wallet object, second get method gets the wallet address...
            String walletAddress = asset.getWalletAddress().getWalletNickname();
            if (!assetsByWallet.containsKey(walletAddress)) {
                assetsByWallet.put(walletAddress, new ArrayList<>());
            }
            AssetDTO assetDTO = new AssetDTO(asset.getTokenName(), asset.getTokenSymbol(), asset.getWalletPrice());
            assetsByWallet.get(walletAddress).add(assetDTO);

        }
        return assetsByWallet;
    }
}
