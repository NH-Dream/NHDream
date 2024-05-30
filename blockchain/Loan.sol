// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "https://github.com/OpenZeppelin/openzeppelin-contracts/blob/v4.4.2/contracts/token/ERC20/IERC20.sol";

contract LoansContract {
    IERC20 public nhdcToken;
    address public owner;
    uint256 public totalActiveLoanAmount; // 현재 총 대출 중인량

    struct Loan {
        uint256 principal; // 원금
        uint256 interest; // 이자
        uint256 totalAmount; // 원리금
        uint256 monthlyPayment; // 매달 상환해야 하는 금액
        uint256 totalPaid; // 현재까지 상환한 금액
        uint32 interestRate; // 이자율 (퍼센트 기준)
        uint256 dueTime; // 상환 기한 (타임스탬프)
        uint32 term; //기간(몇개월인지)
        uint256 lastPaymentTime; // 마지막 상환이 이루어진 시간
        bool isRepaid; // 상환 여부
    }

    mapping(address => mapping(uint32 => Loan)) public loans;

    event LoanIssued(address indexed borrower, uint32 indexed loanId, uint256 principal, uint256 interest, uint256 totalAmount, uint256 monthlyPayment, uint32 interestRate, uint32 term, uint256 dueTime);
    event LoanRepaid(address indexed borrower, uint32 indexed loanId, uint256 amountRepaid);
    event MonthlyPaymentMade(address indexed borrower, uint32 indexed loanId, uint256 amountPaid, uint256 totalPaid);
    event LoanFundsRequested(uint256 amount, uint32 time);

    modifier onlyOwner() {
        require(msg.sender == owner, "Only owner can perform this action");
        _;
    }

    constructor(address _tokenAddress) {
        nhdcToken = IERC20(_tokenAddress);
        owner = msg.sender;
        totalActiveLoanAmount = 0;
    }

    // 대출 발행
    function issueLoan(uint32 _loanId, address _borrower, uint256 _principal, uint256 _interest, uint32 _interestRate, uint32 _term, uint256 _loanPeriodInSeconds, uint256 _monthlyPayment) public onlyOwner {
        require(loans[_borrower][_loanId].principal == 0, "Loan ID already exists");
       
        uint256 dueTime = block.timestamp + _loanPeriodInSeconds;
        uint256 totalAmount = _principal + _interest;


        loans[_borrower][_loanId] = Loan({
            principal: _principal,
            interest: _interest,
            totalAmount: totalAmount,
            monthlyPayment: _monthlyPayment,
            totalPaid: 0,
            interestRate: _interestRate,
            dueTime: dueTime,
            term: _term,
            lastPaymentTime: block.timestamp,
            isRepaid: false
        });

        require(nhdcToken.transfer(_borrower, _principal), "Failed to transfer loan funds");
        totalActiveLoanAmount += _principal;

        emit LoanIssued(_borrower, _loanId, _principal, _interest, totalAmount, _monthlyPayment, _interestRate, _term, dueTime);
    }

    // 매달 상환
    function makeMonthlyPayment(uint32 _loanId) public {
        Loan storage loan = loans[msg.sender][_loanId];
        require(!loan.isRepaid, "Loan already repaid");
        //require(block.timestamp <= loan.dueTime, "Loan is overdue"); 연체는 따로 정한게 없어서 주석처리

        uint256 amountToPay = loan.monthlyPayment;
        require(nhdcToken.transferFrom(msg.sender, address(this), amountToPay), "Failed to make monthly payment");

        loan.totalPaid += amountToPay;
        loan.lastPaymentTime = block.timestamp;

        // 모든 금액을 상환했는지 확인
        uint256 totalAmount = loan.principal + loan.interest;
        if (loan.totalPaid >= totalAmount) {
            loan.isRepaid = true;
            totalActiveLoanAmount -= loan.principal;
            emit LoanRepaid(msg.sender, _loanId, totalAmount);

        }

        emit MonthlyPaymentMade(msg.sender, _loanId, amountToPay, loan.totalPaid);
    }


    // 대출 정보 조회
    function getLoanInfo(address _borrower, uint32 _loanId) public view returns (uint256, uint32, uint256, uint256, uint256, bool) {
        Loan storage loan = loans[_borrower][_loanId];
        return (loan.principal, loan.interestRate, loan.dueTime, loan.monthlyPayment, loan.totalPaid, loan.isRepaid);
    }

    // 총 대출 중인량 조회
    function getTotalActiveLoanAmount() public view returns (uint256) {
        return totalActiveLoanAmount;
    }
}