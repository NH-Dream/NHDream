// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/ERC20.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/access/Ownable.sol";

contract NHDC is ERC20, ERC20Burnable, Ownable {
    uint256 private _totalMinted; //총발행량
    uint256 private _totalBurned; // 총 소각량
    uint256 private _totalInterestdMinted; //총 이자 발행량
    uint256 private _totalInterestPaid; // 총 이자 지급량
    uint256 private _totalLoanMinted; // 대출 총 발행량
    address public interestWallet; // 이자 지갑
    address public loanContract; // 대출 계약주소
    mapping(address => bool) public trustedContracts; //허용할 계약

    constructor(address _interestWallet) ERC20("NHToken", "NHDC") Ownable() {
        _totalMinted = 0; //각 초기값 설정
        _totalBurned = 0;
        _totalInterestdMinted = 0;
        _totalInterestPaid = 0;
        _totalLoanMinted = 0;
        interestWallet = _interestWallet;
        // transferOwnership(initialOwner); // 소유권 이전을 확실히 합니다.
    }

    event Minted(address indexed to, uint256 amount, uint32 indexed time);
    event Burned(address indexed from, uint256 amount, uint32 indexed time);
    event InterestMinted(uint256 amount, uint32 indexed time );
    event InterestTransferred(address indexed to, uint256 amount, uint32 indexed time);
    event LoanMinted(uint256 amount, uint32 indexed time);

    function decimals() public view virtual override returns (uint8) {
        return 0; // 소수점 없는 토큰
    }

    //민팅된 총 금액 보는 함수
    function totalMinted() public view returns (uint256) {
        return _totalMinted;
    }

    //총 소각량 보는 함수
    function totalBurned() public view returns (uint256) {
        return _totalBurned;
    }

    //총 이자용 민팅금액만 보는 함수
    function totalInterestMinted() public view returns (uint256) {
        return _totalInterestdMinted;
    }

    //총 이자 지급한 비용
    function totalInterestPaid() public view returns (uint256) {
        return _totalInterestPaid;
    }
    
    //해당 컨트랙트에 token이용 권리 주는 함수
    function setTrustedContract(address _contract) public onlyOwner {
        trustedContracts[_contract] = true;
    }

    function deleteTrustedContract(address _contract) public onlyOwner {
        trustedContracts[_contract] = false;
    }

    //민트 해주는 함수
    function mint(address to, uint256 amount, uint32 time) public onlyOwner {
        _mint(to, amount);
        _totalMinted += amount;
        emit Minted(to, amount,time);
    }

    //이자전용 민트 함수
    function mintToInterestWallet(uint256 amount, uint32 time) public onlyOwner {
        _mint(interestWallet, amount);
        _totalMinted += amount;
        _totalInterestdMinted += amount;
        emit Minted(interestWallet, amount, time);
        emit InterestMinted(amount, time);
    }

    //대출계약에 대출준비금 지급
    function mintToLoanContract(uint256 amount, uint32 time) public {
        require(loanContract != address(0), "Loan contract address not set");
        _mint(loanContract, amount);
        _totalMinted += amount;
        _totalInterestdMinted += amount;
        emit Minted(interestWallet, amount, time);
        emit InterestMinted(amount, time);
    }

    //소각 함수(자기가 자기 지갑에 있는 만큼)
    function burnWithTime(uint256 amount, uint32 time) public {
        super.burn(amount);
        _totalBurned += amount;
        emit Burned(msg.sender, amount, time);
    }

    //서버에서 강제로 소각하는 함수
    function burnFromWithTime(address account, uint256 amount, uint32 time) public onlyOwner {
        super.burnFrom(account, amount);
        _totalBurned += amount;
        emit Burned(account, amount, time);
    }

    //고객이 직접 이체하는 함수는 내장되어 있음 밑이랑 같다고 생각하면됨
    // function transfer(address recipient, uin256 amount) public returns (bool);

    //이체 함수 서버나 다른 컨트랙트 계약(예금, 적금, 대출)에서
    function transferFrom(address sender, address recipient, uint256 amount) public override returns (bool) {
        // 신뢰된 계약 또는 토큰 소유자 본인이 전송할 수 있도록 허용
        if (trustedContracts[msg.sender] || owner() == msg.sender) {
            _transfer(sender, recipient, amount);
            return true;
        }
        return super.transferFrom(sender, recipient, amount);
    }

    //이자지갑에서 이자주는 함수
    function transferFromInterestWallet(address recipient, uint256 amount, uint32 time) public onlyOwner {
        require(balanceOf(interestWallet) >= amount, "Insufficient balance in interest wallet");
        _transfer(interestWallet, recipient, amount);
        _totalInterestPaid += amount;
        emit InterestTransferred(recipient, amount, time);
    }

    //이자지갑 잔액조회 함수
    function getInterestWalletBalance() public view returns (uint256) {
        return balanceOf(interestWallet);
    }

    //대출컨트랙트 잔액조회 함수
    function getLoanContractBalance() public view returns (uint256) {
        return balanceOf(loanContract);
    }

    //대출함수 정하는 함수
    function setLoanContract(address _loanContractAddress) public onlyOwner {
        loanContract = _loanContractAddress;
    }
}