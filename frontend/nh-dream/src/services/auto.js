import api from "./api";

// 자동이체하기
function sendAuto(data,suc,fail){
  api.post('wallets/autotransfer',data)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}

// 자동이체내역조회하기
function checkAutoHistory(page,size,suc,fail){
  api.get('wallets/autotransfer')
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 자동이체변경하기
function modifyAutoTransfer(data,autoTransferId,suc,fail){
  api.patch(`wallets/autotransfer/${autoTransferId}`,data)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}

// 자동이체해지하기
function terminateAutoTransfer(autoTransferId,suc,fail){
  api.delete(`wallets/autotransfer/${autoTransferId}`)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}

// 자동이체내역상세조회
function checkAutoDetail(autoTransferId,suc,fail){
  api.get(`wallets/autotransfer/${autoTransferId}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

export { sendAuto,checkAutoHistory,modifyAutoTransfer,terminateAutoTransfer,checkAutoDetail }