package com.ssafy.nhdream.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WalletStateResDto {
    private BigInteger microWalletBalance;
    private String microWalletAddress;

    private BigInteger interestWalletBalance;
    private String interestWalletAddress;

    private int personalWalletCount;


}
