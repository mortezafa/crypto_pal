package org.project.cryptopal.respones;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.project.cryptopal.model.WalletAddress;

import java.util.List;

@Getter
@Setter
public class GetAssetsResponse {
    @JsonProperty("code")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<Asset> result;

    @JsonProperty("next_page")
    private Integer nextPage;

    @JsonProperty("count")
    private int count;

    @Getter
    @Setter
    public static class Asset {
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

        @JsonProperty("logos")
        private List<Logo> logos;

        @Getter
        @Setter
        public static class Logo {
            @JsonProperty("uri")
            private String url;

            @JsonProperty("height")
            private int height;

            @JsonProperty("width")
            private int width;
        }
    }

}