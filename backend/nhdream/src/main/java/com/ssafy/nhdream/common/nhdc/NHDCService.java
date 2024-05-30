package com.ssafy.nhdream.common.nhdc;

import com.ssafy.nhdream.common.utils.CalculateDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.ens.EnsResolver;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Async;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NHDCService {


    @Value("${NHDC.contractAddress}")
    private String NHDCContractAddress;

    @Value("${NHDC.privateKey}")
    private String NHDCPrivateKey;

    private NHDCContract loadContract(String privateKey) {
        //블록체인서버주소
        String rpcUrl = "http://k10s209.p.ssafy.io:8545";
        //블록체인 서버 체인Id
        long chainId = 1337;
        //연결할 Web3 인스턴스생성
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        //동적으로 가스비를 조회하고 가스비 설정해줄 가스비조회인스턴스 생성
        DynamicGasProvider dynamicGasProvider = new DynamicGasProvider(web3j);

        //서명생성
        Credentials credentials = Credentials.create(privateKey);

        //트랜잭션 정보 모니터링하는 인스턴스 생성
        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(
                web3j,
                TransactionManager.DEFAULT_POLLING_FREQUENCY,
                TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH
        );

        //Nonce관리, 서명관리, 거래 전송을 담당하는 인스턴스 생성
        TransactionManager transactionManager =new RawTransactionManager(
                web3j,
                credentials,
                chainId,
                receiptProcessor
        );

        //각 인스턴스를 바탕으로 계약 인스턴스 생성
        return NHDCContract.load(NHDCContractAddress, web3j, transactionManager, dynamicGasProvider);
    }

    //잔액 불러오는 함수
    public BigInteger getBalance(String accountAddress, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.balanceOf(accountAddress).send();
    }


    //이체함수 개인이 스스로
    public String transfer(String toAddress, BigDecimal amount, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.transfer(toAddress, amount.toBigInteger()).send().getTransactionHash();
    }

    //이체함수 서버에서 강제로 이체
    public String transferFrom(String sender, String recipient, BigInteger amount, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.transferFrom(sender, recipient, amount).send().getTransactionHash();
    }

    //이자지갑에서 이자주는 함수
    public String transferFromInterestWallet(String recipient, BigInteger amount, BigInteger time, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.transferFromInterestWallet(recipient, amount, time).send().getTransactionHash();
    }

    //소각 함수(자기꺼 스스로 소각) 이거 안쓰고 밑에꺼 쓸 듯
    public String burnWithTime(BigInteger amount, BigInteger time, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.burnWithTime(amount, time).send().getTransactionHash();
    }

    //소각 함수(서버가 개인 지갑에 있는거 소각)
    public String burnFrom(String account, BigInteger amount, BigInteger time, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.burnFromWithTime(account, amount, time).send().getTransactionHash();
    }


    // 민트함수
    public String mint(String to, BigInteger amount, BigInteger time, String privateKey) throws Exception {
        NHDCContract contract = loadContract(privateKey);
        return contract.mint(to, amount, time).send().getTransactionHash();
    }

    // 이자지갑에 민트 함수
    public String mintToInterestWallet(BigInteger amount, BigInteger time) throws Exception {
        NHDCContract contract = loadContract(NHDCPrivateKey);
        log.info("이자지갑에 {} 발급, 시간: {}",amount, LocalDateTime.now());

        return contract.mintToInterestWallet(amount, time).send().getTransactionHash();
    }


    //민트이벤트 불러오는 함수
    public void getMintEventLogs() {
        NHDCContract contract = loadContract(NHDCPrivateKey);
        // 필터링할 블록 범위를 설정
        DefaultBlockParameter startBlock = DefaultBlockParameter.valueOf(BigInteger.valueOf(0)); // 예시로 블록 번호 0부터 시작
        DefaultBlockParameter endBlock = DefaultBlockParameterName.LATEST;
        System.out.println("NHDCService.getMintEventLogs");
        contract.interestMintedEventFlowable(startBlock, endBlock, CalculateDate.changeLocalDatetoBigInteger(LocalDate.now())).subscribe(event -> {
            System.out.println("InterestMinted Event:");
            System.out.println("Time: " + event.time);
            System.out.println("Amount: " + event.amount);
            System.out.println("-------------------");
            System.out.println("event = " + event);
        });
    }









// 안쓸거같은 메서드들
//    public String approve(String spender, BigInteger amount, String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.approve(spender, amount).send().getTransactionHash();
//    }

//    public BigInteger allowance(String owner, String spender, String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.allowance(owner, spender).send();
//    }

//    public String transferOwnership(String newOwner, String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.transferOwnership(newOwner).send().getTransactionHash();
//    }

//    public String increaseAllowance(String spender, BigInteger addedValue, String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.increaseAllowance(spender, addedValue).send().getTransactionHash();
//    }
//
//    public String decreaseAllowance(String spender, BigInteger subtractedValue, String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.decreaseAllowance(spender, subtractedValue).send().getTransactionHash();
//    }
//
//    public BigInteger totalSupply(String privateKey) throws Exception {
//        NHDCContract contract = loadContract(privateKey);
//        return contract.totalSupply().send();
//    }
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
