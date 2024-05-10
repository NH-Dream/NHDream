import api from './api';

function DepositList(success, fail) {
  api.get('/redeposit')
      .then(response => success(response.data.dataBody))
      .catch(error => fail(error))
}

function DepositDetail(id, success, fail) {
  api.get(`/redeposit/${id}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

  export {
    DepositList,
    DepositDetail,
  }