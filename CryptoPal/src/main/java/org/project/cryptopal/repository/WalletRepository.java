package org.project.cryptopal.repository;

import org.project.cryptopal.model.WalletAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletAddress, Long> {
    WalletAddress findByEmail(String email);

}
