import api from '@/services/api'

function getLoanReviewList(success, fail) {
    api.get('/admins/review/loan')
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}


export {
    getLoanReviewList, 
}