package org.project.cryptopal.repository;

import org.project.cryptopal.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("SELECT SUM(a.walletPrice) FROM Asset a")
    Double sumWalletAmount();

    @Query("SELECT a FROM Asset a WHERE a.walletPrice > 0 ORDER BY a.walletPrice DESC")
    List<Asset> getAllAssetsWithPriceMoreThanZero();
}
