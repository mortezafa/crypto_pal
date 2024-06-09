package org.project.cryptopal.repository;

import org.project.cryptopal.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("SELECT SUM(a.walletPrice) FROM Asset a")
    Double getTotalPortfolioPrice();

    @Query("SELECT a FROM Asset a WHERE a.walletPrice > 0 AND a.walletAddress.user.id = :userId ORDER BY a.walletPrice DESC")
    List<Asset> getAllAssetsWithPriceMoreThanZero(@Param("userId") Long userId);
}
