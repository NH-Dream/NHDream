// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/ERC20.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/extensions/ERC20Burnable.sol";
import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/access/Ownable.sol";

contract DRDC is ERC20, ERC20Burnable, Ownable {
    uint256 private _totalMinted;       // 총 발행량
    uint256 private _totalBurned;       // 총 소각량
    uint256 private _totalDRDCPaid;     // 총 지급량

    mapping(address => bool) public trustedContracts;   // DRDC를 사용하는데 허용할 계약

    constructor() ERC20("DRToken", "DRDC") Ownable() {
        _totalMinted = 0;                   // 초기값 세팅
        _totalBurned = 0;
        _totalDRDCPaid = 0;
    }

    event Minted(address indexed to, uint256 amount, uint32 indexed time);
    event Burned(address indexed from, uint256 amount, uint32 indexed time);
    event DRDCTransferred(address indexed to, uint256 amount, uint32 indexed time);

    // 컨트랙트 DRDC 사용 권한 부여 함수
    function setTrustedContract(address _contract) public onlyOwner {
        trustedContracts[_contract] = true;
    }

    // 컨트랙트 DRDC 사용 권한 회수 함수
    function deleteTrustedContract(address _contract) public onlyOwner {
        trustedContracts[_contract] = false;
    }

    function decimals() public view virtual override returns (uint8) {
        return 0; // 소수점 없는 토큰
    }

    // 민팅된 총 금액 보는 함수
    function totalMinted() public view returns (uint256) {
        return _totalMinted;
    }

    // 총 소각량 보는 함수
    function totalBurned() public view returns (uint256) {
        return _totalBurned;
    }

    // 민트 해주는 함수
    function mint(address to, uint256 amount, uint32 time) public onlyOwner {
        _mint(to, amount);
        _totalMinted += amount;
        emit Minted(to, amount, time);
    }

    // 소각 함수(자기가 자기 지갑에 있는 만큼)
    function burnWithTime(uint256 amount, uint32 time) public {
        super.burn(amount);
        _totalBurned += amount;
        emit Burned(msg.sender, amount, time);
    }

    // 서버에서 강제로 소각하는 함수
    function burnFromWithTime(address account, uint256 amount, uint32 time) public onlyOwner {
        super.burnFrom(account, amount);
        _totalBurned += amount;
        emit Burned(account, amount, time);
    }

    // 이체 함수 서버나 다른 컨트랙트 계약(예금, 적금, 대출)에서
    function transferFrom(address sender, address recipient, uint256 amount) public override returns (bool) {
        // 신뢰된 계약 또는 토큰 소유자 본인이 전송할 수 있도록 허용
        if (trustedContracts[msg.sender] || owner() == msg.sender) {
            _transfer(sender, recipient, amount);
            return true;
        }
        return super.transferFrom(sender, recipient, amount);
    }
}