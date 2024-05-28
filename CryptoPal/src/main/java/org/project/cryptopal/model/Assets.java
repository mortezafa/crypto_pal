package org.project.cryptopal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Assets {
    @Id
    private Long id;
    private String tokenName;
    private String tokenSymbol;
    private String tokenQuantity;

    public Assets() {
    }

    public Assets(String tokenName, String tokenSymbol, String tokenQuantity) {
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.tokenQuantity = tokenQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenQuantity() {
        return tokenQuantity;
    }

    public void setTokenQuantity(String tokenQuantity) {
        this.tokenQuantity = tokenQuantity;
    }
}
