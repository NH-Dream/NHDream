package com.ssafy.nhdream.common.drdc;

import com.ssafy.nhdream.common.nhdc.NHDCContract;
import com.ssafy.nhdream.common.nhdc.NHDCService;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DRDCService {

    // 같은거 써도 되는지
    @Value("${DRDC.contractAddress}")
    private String DRDCContractAddress;

    @Value("${NHDC.privateKey}")
    private String DRDCPrivateKey;

    public DRDCContract loadContract(String privateKey) {
        //블록체인서버주소
        String rpcUrl = "http://k10s209.p.ssafy.io:8545";
        //블록체인 서버 체인Id
        long chainId = 1337;
        //연결할 Web3 인스턴스생성
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        //동적으로 가스비를 조회하고 가스비 설정해줄 가스비조회인스턴스 생성
        DRDCService.DynamicGasProvider dynamicGasProvider = new DRDCService.DynamicGasProvider(web3j);

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
        return DRDCContract.load(DRDCContractAddress, web3j, transactionManager, dynamicGasProvider);
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

    //잔액 불러오는 함수
    public BigInteger getBalance(String accountAddress, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.balanceOf(accountAddress).send();
    }


    //이체함수 개인이 스스로
    public String transfer(String toAddress, BigDecimal amount, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.transfer(toAddress, amount.toBigInteger()).send().getTransactionHash();
    }

    //이체함수 서버에서 강제로 이체
    public String transferFrom(String sender, String recipient, BigInteger amount, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.transferFrom(sender, recipient, amount).send().getTransactionHash();
    }

    //소각 함수(자기꺼 스스로 소각) 이거 안쓰고 밑에꺼 쓸 듯
    public String burnWithTime(BigInteger amount, BigInteger time, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.burnWithTime(amount, time).send().getTransactionHash();
    }

    //소각 함수(서버가 개인 지갑에 있는거 소각)
    public String burnFrom(String account, BigInteger amount, BigInteger time, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.burnFromWithTime(account, amount, time).send().getTransactionHash();
    }

    // 민트함수
    public String mint(String to, BigInteger amount, BigInteger time, String privateKey) throws Exception {
        DRDCContract contract = loadContract(privateKey);
        return contract.mint(to, amount, time).send().getTransactionHash();
    }

    // 기간별 DRDC 발급량 조회용 메서드
    public List<BigInteger> getMintedEventFlowable(LocalDate fromDate, LocalDate toDate) {

        DRDCContract contract = loadContract(DRDCPrivateKey);

        int formatedFromDate = Integer.parseInt(fromDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int formatedToDate = Integer.parseInt(toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        log.info("FromDate : {} , ToDate : {}", formatedFromDate, formatedToDate);

        Period period = Period.between(fromDate, toDate);

        // 합계 저장할 리스트
        List<BigInteger> mintedList = new ArrayList<>();

        // 합계 구하는 로직
        for (int i = 0 ; i <= period.getDays() ; i++) {

            // 해당 일자에 대한 로그내역들 불러오기
            Flowable<DRDCContract.MintedEventResponse> mintedEventResponseFlowable =
                    contract.mintedEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, formatedFromDate + i);

            // 합계 구할 변수
            BigInteger amountSum = new BigInteger("0");

            // 발행량 더해주기
            mintedEventResponseFlowable.subscribe(mintedEventResponse -> {
                amountSum.add(mintedEventResponse.amount);
            }, error -> {
                log.error("[mintedEventResponseFlowable] Error: {}", error.getMessage());
            });

            // 리스트에 추가
            mintedList.add(amountSum);
        }

        return mintedList;
    }

    // 기간별 DRDC 소각량 조회 메서드
    public List<BigInteger> getBurnedEventFlowable(LocalDate fromDate, LocalDate toDate) {

        DRDCContract contract = loadContract(DRDCPrivateKey);

        int formatedFromDate = Integer.parseInt(fromDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int formatedToDate = Integer.parseInt(toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        log.info("FromDate : {} , ToDate : {}", formatedFromDate, formatedToDate);

        Period period = Period.between(fromDate, toDate);

        // 합계 저장할 리스트
        List<BigInteger> burnedList = new ArrayList<>();

        // 합계 구하는 로직
        for (int i = 0 ; i <= period.getDays() ; i++) {

            // 해당 일자에 대한 로그내역들 불러오기
            Flowable<DRDCContract.BurnedEventResponse> burnedEventResponseFlowable =
                    contract.burnedEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, formatedFromDate + i);

            // 합계 구할 변수
            BigInteger amountSum = new BigInteger("0");

            // 소각량 더해주기
            burnedEventResponseFlowable.subscribe(burnedEventResponse -> {
                amountSum.add(burnedEventResponse.amount);
            }, error -> {
                log.error("[mintedEventResponseFlowable] Error: {}", error.getMessage());
            });

            // 리스트에 추가
            burnedList.add(amountSum);
        }

        return burnedList;
    }
}
