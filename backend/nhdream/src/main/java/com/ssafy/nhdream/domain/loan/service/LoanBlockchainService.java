package com.ssafy.nhdream.domain.loan.service;

import com.ssafy.nhdream.domain.loan.contract.LoanContract;
import com.ssafy.nhdream.domain.redeposit.contract.ReDepositContract;
import com.ssafy.nhdream.domain.redeposit.service.RedepositBlockchainService;
import com.ssafy.nhdream.entity.user.User;
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
public class LoanBlockchainService {

    @Value("${loan.contractAddress}")
    private String loanContractAddress;
    @Value("${NHDC.privateKey}")
    private String serverPrivateKey;
    private LoanContract loadContract(String privateKey) {
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

        return LoanContract.load(loanContractAddress, web3j, transactionManager, dynamicGasProvider);
    }

    public static class DynamicGasProvider implements ContractGasProvider {
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


    //대출가입
    //uint32 _loanId, address _borrower, uint256 _principal, uint256 _interest, uint32 _interestRate, uint32 _term, uint256 _loanPeriodInSeconds, uint256 _monthlyPayment
    public String issueLoan(int loanProductId, String loanApplicantWalletAddress, BigDecimal principal, BigDecimal interest, BigDecimal interestRate, int term, BigInteger periodInSeconds, BigDecimal monthlyPayment) throws Exception {

        LoanContract loanContract = loadContract(serverPrivateKey);
        BigDecimal interestToInt = interestRate.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.DOWN);

        return loanContract.issueLoan(BigInteger.valueOf(loanProductId), loanApplicantWalletAddress, principal.toBigInteger(), interest.toBigInteger(), interestToInt.toBigInteger(), BigInteger.valueOf(term), periodInSeconds, monthlyPayment.toBigInteger()).send().getTransactionHash();
    }


    //대출상환
    public String makeMonthlyPayment(int loanProductId, String privateKey) throws Exception {

        LoanContract loanContract = loadContract(privateKey);
        return loanContract.makeMonthlyPayment(BigInteger.valueOf(loanProductId)).send().getTransactionHash();
    }


}
