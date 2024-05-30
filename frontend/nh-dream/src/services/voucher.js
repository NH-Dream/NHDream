import api from "./api"

// 카테고리별 상품목록조회
function checkProductList(type,suc,fail){
api.get(`vouchers/items?type=${type}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 상품상세조회
function checkProductDetail(itemId,suc,fail){
  api.get(`vouchers/items/${itemId}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 바우처토큰잔액 조회
function checkVoucherBalance(loginId,suc,fail){
  api.get(`vouchers/token/${loginId}`)
  .then(res=>suc(res.data.dataBody))
  .catch(err=>fail(err))
}

// 바우처 상품결제
function buyVoucherProduct(data,suc,fail){
  api.post('vouchers/buy',data)
  .then(res=>suc(res))
  .catch(err=>fail(err))
}

export { checkProductList,checkProductDetail,checkVoucherBalance,buyVoucherProduct }