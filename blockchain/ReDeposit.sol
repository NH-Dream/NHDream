// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0; // 스마트 계약 선언

// 토큰 가져오기
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/IERC20.sol"; 

// 스마트 컨트랙트 선언
contract ReDepositContract {
    
    // 변수 선언
    IERC20 public nhdcToken;    // 농협토큰
    address public owner;       // 컨트랙트 배포한 주소

    // 컨트랙트 생성자
    constructor(address _tokenAddress) { // _tokenAddress nhcd 스마트컨트랙트 계약주소
        nhdcToken = IERC20(_tokenAddress);  
        owner = msg.sender;
    }

    // 정기예금 구조체 정의
    struct ReDepositAccount {
        uint256 balance;        // 잔액
        uint256 interestRate;   // 이자율(기록용이지 계산은 X) 0.05 -> 5 로 입력
        uint256 maturityTime;   // 만기일
        bool isMatured;         // 만료여부
    }

    // 매핑 : 사용자 주소 기반으로 각 계좌에 접근
    mapping(address => mapping(uint32 => ReDepositAccount)) public accounts;

    // 이벤트 정의
    event NewAccount( // 새 계좌가 생성될 때 필요한 정보를 로깅(블록 안에 로깅) - 모니터링
        address indexed user, 
        uint32 optionId, 
        uint256 balance, 
        uint256 interestRate, 
        uint256 maturityTime
    );

    event PrincipalReturned( // 원금이 반환될 때 발생하며, 관련 정보를 로깅
        address indexed user, 
        uint32 optionId, 
        uint256 amountReturned
    );

    
    // 계좌 개설
    function openAccount(uint32 _optionId, uint256 _amount, uint256 _interestRate, uint256 _maturityPeriodInSeconds) public {
        require(nhdcToken.transferFrom(msg.sender, address(this), _amount), "Transfer failed");
        uint256 maturityTime = block.timestamp + _maturityPeriodInSeconds;  // 인덱싱을 위한 optionId , 만료일자 전후 확인용 시간
        accounts[msg.sender][_optionId] = ReDepositAccount(_amount, _interestRate, maturityTime, false);
        emit NewAccount(msg.sender, _optionId, _amount, _interestRate, maturityTime);
    }


    // 원금 반환
    function returnPrincipal(uint32 _optionId) public {
        ReDepositAccount storage account = accounts[msg.sender][_optionId];
        require(block.timestamp >= account.maturityTime, "Account not matured yet");            // 아직 만기일 전일 경우
        require(account.isMatured == false, "Account already settled");                         // 원금 회수 후
        require(nhdcToken.transfer(msg.sender, account.balance), "Failed to return principal"); // 전송
        account.isMatured = true;
        emit PrincipalReturned(msg.sender, _optionId, account.balance);
    }

    // 원금 입금
    function deposit(uint32 _optionId, uint256 _amount) public {
        ReDepositAccount storage account = accounts[msg.sender][_optionId];
        require(account.isMatured == false, "Account has already matured");
        require(nhdcToken.transferFrom(msg.sender, address(this), _amount), "Transfer failed");
        account.balance += _amount;
    }

}