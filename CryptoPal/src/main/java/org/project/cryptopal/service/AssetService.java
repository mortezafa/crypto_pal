package org.project.cryptopal.service;

import org.project.cryptopal.model.Asset;
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
       String walletAddress = authService.getWalletAddressByEmail(email);
       if (walletAddress.isEmpty()) {
           throw new RuntimeException("User with email: " + email + "Has not registerd their wallet yet");
       }
        int page = 1;
        boolean morePages = true;
        ArrayList<Asset> allAssets = new ArrayList<>();
        while(morePages) {
            String url = String.format("https://api.chainbase.online/v1/account/tokens?chain_id=1&address=%s&contract_address=&limit=100&page=%d", walletAddress, page);
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", "demo");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GetAssetsResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, GetAssetsResponse.class);
            System.out.println("Response: " + response.getBody());
            GetAssetsResponse getAssetsResponse = response.getBody();


            if (getAssetsResponse != null && getAssetsResponse.getResult() != null){
                allAssets.addAll(getAssetsResponse.getResult());
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
