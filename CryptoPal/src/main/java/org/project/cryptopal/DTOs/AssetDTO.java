package org.project.cryptopal.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssetDTO {
    private String tokenName;
    private String tokenSymbol;
    private Double walletPrice;
//    private String walletNickname;

}
