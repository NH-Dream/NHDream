import api from '@/services/api'

// NHDC
function getNHDCFlow(startDate, endDate, success, fail) {
    api.get(`/admins/nhdc?startDate=${startDate}&endDate=${endDate}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}


export {
    getNHDCFlow, 
}