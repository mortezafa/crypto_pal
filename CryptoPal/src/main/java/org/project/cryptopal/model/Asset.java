package org.project.cryptopal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.json.JsonPolymorphicTypeMap;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.project.cryptopal.respones.GetAssetsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "asset_table")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private List<GetAssetsResponse.Asset.Logo> logosList;
}