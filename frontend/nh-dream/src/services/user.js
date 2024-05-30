import api from '@/services/api';

// 아이디 중복검사
function checkDuplicateId(id, success, fail) {
api.get(`/users/duplicateId?id=${id}`)
    .then(response => success(response.data))
    .catch(error => fail(error))
}

// 이메일 발송
function sendEmail(data, success, fail) {
api.post(`/users/email`, data)
    .then(response => success(response.data))
    .catch(error => fail(error))
}

// 이메일 인증
function verifyCode(data, success, fail) {
api.post(`/users/auth/check`, data)
    .then(response => success(response.data))
    .catch(error => fail(error))
}

// 회원가입
function signUp(data, success, fail) {
api.post(`/users`, data)
    .then(response => success(response.data))
    .catch(error => fail(error))
}

// 교육 인증
function applyEducation(formData, success, fail) {
api.post(`/users/apply/education`, formData, {
    headers: {
    'Content-Type': 'multipart/form-data'
    }
})
    .then(response => success(response.data))
    .catch(error => fail(error))
}

// 농부 인증
function applyFarmer(formData, success, fail) {
api.post(`/users/apply/farmer`, formData, {
    headers: {
    'Content-Type': 'multipart/form-data'
    }
})
    .then(response => success(response.data))
    .catch(error => fail(error.response.data.dataHeader.resultCode))
}

// 로그인
function login(data, success, fail) {
api.post(`users/login`, data)
    .then(response => success(response))
    .catch(error => fail(error))
}

// 로그아웃
function logout(success, fail) {
api.post(`users/logout`)
    .then(response => success(response))
    .catch(error => fail(error))
}

// 회원정보조회
function checkUser(suc,fail){
    api.get('users')
    .then(res=>suc(res.data.dataBody))
    .catch(err=>fail(err))
}

// 지갑 비밀번호
function walletPasswordCheck(password, success, fail) {
    api.post('/users/walletpassword', password)
    .then(response => success(response))
    .catch(error => fail(error))
}

// 비밀번호 변경
function passwordChange(password, success, fail) {
    api.patch('/users/password/update', password)
    .then(response => success(response))
    .catch(error => fail(error))
}

// 회원정보 받아오기
function getUserInfo(success, fail) {
    api.get('/users')
        .then(response => success(response.data.dataBody))
        .catch(error => fail(error))
    }

// 농부인증신청
function iWantToBeFarmer(myInfo, success, fail) {
    api.patch('/users/me/type', myInfo)
    .then(response => success(response))
    .catch(error => fail(error))
}

// 통합거래내역
function allTransactionList(id, success, fail) {
    api.get(`/loans/transaction?id=${id}`)
    .then(response => success(response.data.dataBody))
    .catch(error => fail(error))
}

export {
    checkDuplicateId,
    sendEmail,
    verifyCode,
    signUp,
    applyEducation,
    applyFarmer,
    login,
    logout,
    checkUser,
    walletPasswordCheck,
    passwordChange,
    getUserInfo,
    iWantToBeFarmer,
    allTransactionList,
}