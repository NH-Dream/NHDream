import api from '@/services/api'

// 대출 목록 조회
function getLoanReviewList(pageNum, status, userName, success, fail) {
    api.get(`/admins/review/loan?page=${pageNum}&status=${status}&userName=${userName}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 대출 상세 조회
function getLoanReviewDetail(loanReviewId, success, fail) {
    api.get(`/admins/review/loan/${loanReviewId}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 대출 심사
function sendReviewResult(reviewResult, success, fail) {
    api.patch(`/admins/loan`, reviewResult)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 교육 목록 조회
function getTrainingReviewList(pageNum, status, userName, success, fail) {
    api.get(`/admins/review/training?page=${pageNum}&status=${status}&userName=${userName}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 귀농 목록 조회
function getFarmerReviewList(pageNum, userName, success, fail) {
    api.get(`/admins/review/farmer?page=${pageNum}&userName=${userName}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 귀농 상세 조회
function getFarmerReviewDetail(farmerReviewId, success, fail) {
    api.get(`/admins/review/farmer/${farmerReviewId}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 교육 상세 조회
function getTrainingReviewDetail(trainingReviewId, success, fail) {
    api.get(`/admins/review/training/${trainingReviewId}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

// 대출 심사
function sendTrainingReviewResult(reviewResult, success, fail) {
    api.patch(`/admins/training`, reviewResult)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

export {
    getLoanReviewList, 
    getLoanReviewDetail,
    sendReviewResult, 
    getTrainingReviewList,
    getFarmerReviewList, 
    getFarmerReviewDetail,
    getTrainingReviewDetail,
    sendTrainingReviewResult,
}