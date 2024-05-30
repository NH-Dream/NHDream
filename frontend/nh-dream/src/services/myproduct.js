import api from "./api"

// 내가 가입한 상품(예적금)조회
function checkMyproduct(suc,fail){
  api.get('users/account')
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 내가 가입한 적금의 거래기록보기
function checkSavingHistory(savingAccountId,page,size,suc,fail){
  api.get(`savings/${savingAccountId}/transactions?page=${page}&size=${size}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}


// 내가 가입한 예금의 거래기록보기
function checkDepositHistory(depositAccountId,suc,fail){
  api.get(`redeposit/transfer/${depositAccountId}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}


// 가입한적금계좌상세화면보기
function checkSavingDetail(savingAccountId,suc,fail){
  api.get(`savings/${savingAccountId}/detail`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 가입한예금계좌상세화면보기
function checkDepositDetail(depositAccountId,suc,fail){
  api.get(`redeposit/detail/${depositAccountId}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 내가 가입한 대출목록 조회하기
function checkMyloan(id,suc,fail){
  api.get(`loans/mylist?id=${id}`)
  .then(res=>suc(res.data.dataBody.myList))
  .catch(err=>fail(err))
}

// 승인된 대출 상세 정보 
function checkMyloanDetail(id,suc,fail){
  api.get(`loans/myloan?id=${id}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

export { checkMyproduct,checkSavingHistory,checkSavingDetail,
  checkMyloan,checkDepositHistory,checkDepositDetail,checkMyloanDetail }