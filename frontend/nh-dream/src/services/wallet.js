import api from "./api"

//지갑잔액조회
function checkWalletBalance(suc,fail){
api.get('/wallets')
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

//지갑주소로 계좌주조회
function whoSending(walletAddress, suc, fail){
api.get(`wallets/${walletAddress}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

//지갑주소로 계좌주조회+시작일
function whoSendingDate(walletAddress,transferDate, suc, fail){
  api.get(`wallets/${walletAddress}?transferDate=${transferDate}`)
    .then(res=>suc(res.data.dataBody))
    .catch(err=>fail(err))
  }



// 지갑거래내역조회
function checkWalletHistory(walletAddress,page,size, suc, fail){
api.get(`wallets/${walletAddress}/transactions?page=${page}&size=${size}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

//계좌이체
function sendNhdc(data,suc,fail){
api.post('wallets',data)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

//지갑비밀번호 변경
function changeWalletPassword(data,suc,fail){
  api.patch('users/walletpassword/update',data)
  .then(res=>suc(res))
  .catch(err=>fail(err.response.data.dataHeader.resultCode))
}

// 지갑비밀번호 검증
function confrimWalletPassword(data,suc,fail){
  api.post('users/walletpassword',data)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}

// 최근에보낸주소조회
function recentTransferHistory(suc,fail){
  api.get('wallets/list')
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

export { checkWalletBalance,whoSending,whoSendingDate,checkWalletHistory,
  sendNhdc,changeWalletPassword,confrimWalletPassword,recentTransferHistory }
