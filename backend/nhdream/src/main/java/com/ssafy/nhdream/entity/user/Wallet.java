package com.ssafy.nhdream.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Wallet {

    //지갑 주소
    @Column(name = "user_wallet_address", unique = true)
    private String walletAddress;

    //개인 키
    @Column(name = "user_wallet_privatekey", unique = true)
    private String walletPrivateKey;

    @Builder
    public Wallet(String walletAddress, String walletPrivateKey) {
        this.walletAddress = walletAddress;
        this.walletPrivateKey = walletPrivateKey;
    }
}
