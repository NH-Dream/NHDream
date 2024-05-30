import Web3 from "web3";

//web3 인스턴스생성
const web3 = new Web3('http://k10s209.p.ssafy.io:8545/')

//Get함수만 따로 뺀 ABI
const contractABI =[
    {
        "inputs": [],
        "name": "decimals",
        "outputs": [
            {
                "internalType": "uint8",
                "name": "",
                "type": "uint8"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "getInterestWalletBalance",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "interestWallet",
        "outputs": [
            {
                "internalType": "address",
                "name": "",
                "type": "address"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "loanContract",
        "outputs": [
            {
                "internalType": "address",
                "name": "",
                "type": "address"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "totalBurned",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "totalInterestMinted",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "totalInterestPaid",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "totalMinted",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    },
    {
        "inputs": [],
        "name": "totalSupply",
        "outputs": [
            {
                "internalType": "uint256",
                "name": "",
                "type": "uint256"
            }
        ],
        "stateMutability": "view",
        "type": "function"
    }
]

//계약주소
const contractAddress = '0x1D0C65C898334649e960031577B4D0FcD5661Aa7'

//계약 인스턴스생성
const contract = new web3.eth.Contract(contractABI,contractAddress)

// 민팅된 총 금액(=총 발행량)조회 함수
function totalMinted(success, fail) {
  contract.methods.totalMinted().call()
  .then((result) => success(result))
  .catch((error) => fail(error))
}

// 총 소각량조회 함수
function totalBurned(success, fail) {
  contract.methods.totalBurned().call()
  .then((result) => success(result))
  .catch((error) => fail(error))
}

//이자지갑 잔액조회 함수
function getInterestWalletBalance(success, fail) {
  contract.methods.getInterestWalletBalance().call()
  .then((result) => success(result))
  .catch((error) => fail(error))
}

export {
  totalMinted,
  totalBurned,
  getInterestWalletBalance
}
