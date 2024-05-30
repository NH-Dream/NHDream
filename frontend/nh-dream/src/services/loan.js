import api from './api';

function LoanList(success, fail) {
  api.get('/loans/list')
  .then(response => success(response.data.dataBody.loanListDtos))
  .catch(error => fail(error))
}

function LoanDetail(id, success, fail) {
  api.get(`/loans/product/${id}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

function LoanJoin1(loanInfo, success, fail) {
  api.post('/loans/option', loanInfo)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

function LoanJoinInfo(optionId, success, fail) {
  api.get(`/loans/option?id=${optionId}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

function LoanJudge(userId, optionId, document, success, fail) {
  api.post(`/loans/approval?userId=${userId}&loanOptionId=${optionId}`, document)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

export {
  LoanList,
  LoanDetail,
  LoanJoin1,
  LoanJoinInfo,
  LoanJudge
}