package org.project.cryptopal.controller;

import org.project.cryptopal.model.Asset;
import org.project.cryptopal.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/requestAssets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/getAssets")
    public ResponseEntity<?> getAssetsByEmail(@RequestParam String email) {
        try {
            List<Asset> assets = assetService.fetchUserAssets(email);
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getTotalWalletPrice")
    public ResponseEntity<Double> fetchTotalWalletPrice() {
        Double totalWalletPrice = assetService.getTotalWalletPrice();
        return ResponseEntity.ok(totalWalletPrice);
    }

    @GetMapping("/getAssetsPrice")
    public ResponseEntity<?> fetchAssetsMoreThanZero(){
        List<Asset> allAssetsMoreThanZero = assetService.getAssetsMoreThanZero();
        return ResponseEntity.ok(allAssetsMoreThanZero);
    }

}

