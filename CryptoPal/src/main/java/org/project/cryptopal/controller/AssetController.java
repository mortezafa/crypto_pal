package org.project.cryptopal.controller;

import org.project.cryptopal.DTOs.AssetDTO;
import org.project.cryptopal.model.Asset;
import org.project.cryptopal.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/requestAssets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/getAssets")
    public ResponseEntity<?> getAssetsByEmail(@RequestParam Long id) {
        try {
            List<Asset> assets = assetService.fetchUserAssets(id);
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/totalPortfolioAmount")
    public ResponseEntity<Double> fetchTotalWalletPrice() {
        Double totalWalletPrice = assetService.getTotalWalletPrice();
        return ResponseEntity.ok(totalWalletPrice);
    }

    @GetMapping("/getAssetsPrice")
    public ResponseEntity<?> fetchAssetsMoreThanZero(@RequestParam Long id) {
        Map<String, List<AssetDTO>> allAssetsMoreThanZero = assetService.getAssetsMoreThanZero(id);
        return ResponseEntity.ok(allAssetsMoreThanZero);
    }

}

