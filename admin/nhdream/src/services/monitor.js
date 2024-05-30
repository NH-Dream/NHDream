import api from '@/services/api'

// NHDC 유동량
function getNHDCFlow(startDate, endDate, success, fail) {
  api.get(`/admins/nhdc?startDate=${startDate}&endDate=${endDate}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

// NHDC 거래량
function getNHDCTradeFlow(startDate, endDate, success, fail) {
    api.get(`/admins/nhdc/transaction?startDate=${startDate}&endDate=${endDate}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 드림토큰 불러오기
function getDRDCFlow(fromDate, toDate, success, fail) {
  api.get(`/vouchers/admin?fromDate=${fromDate}&toDate=${toDate}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

export {
    getNHDCFlow,
    getNHDCTradeFlow, 
    getDRDCFlow,
}