package com.ssafy.nhdream.domain.redeposit.service;

import com.ssafy.nhdream.domain.redeposit.contract.ReDepositContract;
import com.ssafy.nhdream.domain.saving.contract.SavingContract;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class RedepositBlockchainService {

    @Value("${redeposit.contractAddress}")
    private String contractAddress;

    private ReDepositContract loadContract(String privateKey) {
        //web3j 인스턴스 생성
        Web3j web3j = Web3j.build(new HttpService("http://k10s209.p.ssafy.io:8545"));
        //개인키바탕으로 서명 생성
        Credentials credentials = Credentials.create(privateKey);
        //서버 chainId
        long chainId = 1337;
        //동적가스비조회기 인스턴스 생성
        DynamicGasProvider dynamicGasProvider = new DynamicGasProvider(web3j);
        //트랜잭션기록 관리 인스턴스 생성
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                web3j,
                TransactionManager.DEFAULT_POLLING_FREQUENCY,
                TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );
        //트랜잭션관리 인스턴스 생성
        TransactionManager transactionManager =new RawTransactionManager(
                web3j,
                credentials,
                chainId,
                receiptProcessor
        );

        return ReDepositContract.load(contractAddress, web3j, transactionManager, dynamicGasProvider);
    }

    //계좌생성 메서드
    public String openAccount(int savingOptionId, BigInteger amount, BigDecimal interestRate, BigInteger maturityPeriodInSeconds, String privateKey) throws Exception {
        ReDepositContract reDepositContract = loadContract(privateKey);
        //정수값으로 변형
        BigInteger scaledInterestRate = interestRate.multiply(new BigDecimal("100")).setScale(0, RoundingMode.DOWN).toBigInteger();

        return reDepositContract.openAccount(BigInteger.valueOf(savingOptionId), amount, scaledInterestRate, maturityPeriodInSeconds).send().getTransactionHash();
    }

    // 정기 예금 원금 이체
    public String depositPrincipalAmount(int optionId,BigDecimal amount,String privateKey) throws Exception {
        ReDepositContract reDepositContract = loadContract(privateKey);
        return reDepositContract.deposit(BigInteger.valueOf(optionId), amount.toBigInteger()).send().getTransactionHash();
    }

    //계좌만기해지 메서드
    public String returnPrincipal(int savingOptionId,String privateKey) throws Exception {
        ReDepositContract reDepositContract = loadContract(privateKey);
        return reDepositContract.returnPrincipal(BigInteger.valueOf(savingOptionId)).send().getTransactionHash();
    }

    public class DynamicGasProvider implements ContractGasProvider {
        private final Web3j web3j;

        public DynamicGasProvider(Web3j web3j) {
            this.web3j = web3j;
        }

        @Override
        public BigInteger getGasPrice(String contractFunc) {
            try {
                EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
                return ethGasPrice.getGasPrice();
            } catch (IOException e) {
                throw new RuntimeException("Unable to fetch gas price", e);
            }
        }

        @Override
        public BigInteger getGasPrice() {
            return getGasPrice(null);
        }

        @Override
        public BigInteger getGasLimit(String contractFunc) {
            // 여기서는 일반적인 가스 한도를 사용할 수 있지만, 필요에 따라 다르게 설정 가능
            return BigInteger.valueOf(6721975);
        }

        @Override
        public BigInteger getGasLimit() {
            return getGasLimit(null);
        }
    }
}
