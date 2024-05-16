import api from '@/services/api'

function individualWallet(success, fail) {
  api.get('/admins/nhdc/wallet-count')
  .then(response => success(response.data.dataBody))
  .catch(error => fail(error))
}

export {
  individualWallet,
}