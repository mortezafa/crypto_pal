package org.project.cryptopal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
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
    private String walletNickname;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)

    private User user;
    //TODO: ask zealous if doing this only gives you access to id... like my other addwalletAddress function i just set user. Does that only set the ID cause i did "name = id"?


    @OneToMany(mappedBy = "walletAddress")
    private List<Asset> assets;
    // TODO: CHANGE CIRUCLAR REFERANCE


}
