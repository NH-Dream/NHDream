import api from './api';

// 적금 전체 목록
function SavingList(success, fail) {
  api.get('/savings')
      .then(response => success(response.data.dataBody))
      .catch(error => fail(error))
}

// 적금 상세 조회
function SavingDetail(id, success, fail) {
  api.get(`/savings/${id}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

// 적금 가입
function SavingJoin(id, savingInfo, success, fail) {
  api.post(`/savings/${id}`, savingInfo)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error.response.data.dataHeader))
}

  export {
    SavingList,
    SavingDetail,
    SavingJoin,
  }