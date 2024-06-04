package org.project.cryptopal.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "wallet_address")
public class WalletAddress {

    @Id
    @SequenceGenerator(
            name = "wallet_sequence",
            sequenceName = "wallet_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wallet_sequence"
    )
    private Long walletId;
    private String walletAddress;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "walletAddress")
    private List<Asset> assets;


    public WalletAddress() {}

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
