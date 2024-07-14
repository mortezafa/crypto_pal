package org.project.cryptopal.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.project.cryptopal.respones.GetAssetsResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AssetDTO {
    private String tokenName;
    private String tokenSymbol;
    private Double walletPrice;
    private List<GetAssetsResponse.Asset.Logo> logos;
}
