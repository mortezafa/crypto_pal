package org.project.cryptopal.service;

import org.project.cryptopal.model.Asset;
import org.project.cryptopal.model.WalletAddress;
import org.project.cryptopal.repository.AssetRepository;
import org.project.cryptopal.repository.UserRepository;
import org.project.cryptopal.respones.GetAssetsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private FirebaseAuthService authService;


    public List<Asset> fetchUserAssets(String email){
        List<WalletAddress> walletAddresses = authService.getWalletAddressesByEmail(email);
        if (walletAddresses.isEmpty()) {
           throw new RuntimeException("User with email: " + email + "Has not registerd their wallet yet");
       }
        System.out.println("HELLO MEOWWWWWW " + walletAddresses);
        ArrayList<Asset> allAssets = new ArrayList<>();
       for (WalletAddress walletAddress : walletAddresses) {
        int page = 1;
        boolean morePages = true;
        while(morePages) {
            String url = String.format("https://api.chainbase.online/v1/account/tokens?chain_id=1&address=%s&contract_address=&limit=100&page=%d", walletAddress, page);
            System.out.println("THIS IS THE URLLLLLLL" + url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", "2hFidnC2jl29UjzHNmcLqWY4RLv");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GetAssetsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetAssetsResponse.class); //THIS IS WHERE PROGRAM BREAKS
            GetAssetsResponse getAssetsResponse = response.getBody();


            if (getAssetsResponse != null && getAssetsResponse.getResult() != null){
                for (Asset asset : getAssetsResponse.getResult()) {
                    asset.setWalletAddress(walletAddress); // Set the wallet address for each asset
                    allAssets.add(asset);
                }
                if (getAssetsResponse.getNextPage() != null) {
                    page = getAssetsResponse.getNextPage();
                } else {
                    morePages = false;
                }
            } else {
                morePages = false;
                throw new RuntimeException("Failed to fetch from API.");
            }
        }
       }
        assetRepository.saveAll(allAssets);
        return allAssets;
    }

    public Double getTotalWalletPrice() {
        return assetRepository.sumWalletAmount();
    }

    public List<Asset> getAssetsMoreThanZero() {
        return assetRepository.getAllAssetsWithPriceMoreThanZero();
    }
}
