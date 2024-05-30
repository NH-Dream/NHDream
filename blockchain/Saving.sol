// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/IERC20.sol";

contract SavingsContract {
    IERC20 public nhdcToken;
    address public owner;

    struct SavingsAccount {
        uint256 balance;
        uint256 interestRate;
        uint256 maturityTime;
        bool isMatured;
    }

    mapping(address => mapping(uint32 => SavingsAccount)) public accounts;

    event NewAccount(address indexed user,uint32 optionId, uint256 balance, uint256 interestRate, uint256 maturityTime);
    event PrincipalReturned(address indexed user, uint32 optionId, uint256 amountReturned);

    constructor(address _tokenAddress) {
        nhdcToken = IERC20(_tokenAddress);
        owner = msg.sender;
    }

    function openAccount(uint32 _optionId, uint256 _amount, uint256 _interestRate, uint256 _maturityPeriodInSeconds) public {
        require(nhdcToken.transferFrom(msg.sender, address(this), _amount), "Transfer failed");
        uint256 maturityTime = block.timestamp + (_maturityPeriodInSeconds);
        accounts[msg.sender][_optionId] = SavingsAccount(_amount, _interestRate, maturityTime, false);
        emit NewAccount(msg.sender, _optionId, _amount, _interestRate, maturityTime);
    }


    function returnPrincipal(uint32 _optionId) public {
    SavingsAccount storage account = accounts[msg.sender][_optionId];
    require(block.timestamp >= account.maturityTime, "Account not matured yet");
    require(account.isMatured == false, "Account already settled");
    require(nhdcToken.transfer(msg.sender, account.balance), "Failed to return principal");
    account.isMatured = true;
    emit PrincipalReturned(msg.sender, _optionId, account.balance);
    }

    
    function deposit(uint32 _optionId, uint256 _amount) public {
        SavingsAccount storage account = accounts[msg.sender][_optionId];
        require(account.isMatured == false, "Account has already matured");
        require(nhdcToken.transferFrom(msg.sender, address(this), _amount), "Transfer failed");
        account.balance += _amount;
    }
}
