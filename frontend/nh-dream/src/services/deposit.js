import api from './api';

// 예금 전체 목록 - 이자 소수점
function DepositList(success, fail) {
  api.get('/redeposit')
      .then(response => success(response.data.dataBody))
      .catch(error => fail(error))
}

// 예금 상세 목록 - 이자 *100 한 상태
function DepositDetail(id, success, fail) {
  api.get(`/redeposit/${id}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

function DepositJoin(id, depositInfo, success, fail) {
  api.post(`/redeposit/${id}`, depositInfo)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error.response.data.dataHeader))
}
  export {
    DepositList,
    DepositDetail,
    DepositJoin
  }