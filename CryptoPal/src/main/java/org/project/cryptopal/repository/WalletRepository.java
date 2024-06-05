package org.project.cryptopal.repository;

import org.project.cryptopal.model.WalletAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletAddress, Long> {
    List<WalletAddress> findByUserId(Long userId);

}
