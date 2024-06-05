package org.project.cryptopal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "asset_table")
public class Asset {
    @Id
    @SequenceGenerator(
            name = "asset_sequence",
            sequenceName = "asset_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "asset_sequence"
    )
    private Long id;

    @JsonProperty("name")
    private String tokenName;

    @JsonProperty("current_usd_price")
    private Double walletPrice;

    @JsonProperty("balance")
    private String tokenQuantity;

    @JsonProperty("contract_address")
    private String contractAddress;

    @JsonProperty("decimals")
    private Integer decimals;

    @JsonProperty("symbol")
    private String tokenSymbol;

    @JsonProperty("total_supply")
    private String totalSupply;

    @ManyToOne
    @JoinColumn(name = "walletId", nullable = false)
    private WalletAddress walletAddress;


    public Asset() {}

    public Asset(String tokenName, Double walletPrice, String tokenQuantity) {
        this.tokenName = tokenName;
        this.walletPrice = walletPrice;
        this.tokenQuantity = tokenQuantity;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Double getWalletPrice() {
        return walletPrice;
    }

    public void setWalletPrice(Double walletPrice) {
        this.walletPrice = walletPrice;
    }

    public String getTokenQuantity() {
        return tokenQuantity;
    }

    public void setTokenQuantity(String tokenQuantity) {
        this.tokenQuantity = tokenQuantity;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }


    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public WalletAddress getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(WalletAddress walletAddress) {
        this.walletAddress = walletAddress;
    }
}
