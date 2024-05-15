import api from '@/services/api'

// 드림토큰 불러오기
function getDRDC(fromDate, toDate, success, fail) {
  api.get(`/vouchers/admin?fromDate=${fromDate}&toDate=${toDate}`)
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

export {
  getDRDC, 
}