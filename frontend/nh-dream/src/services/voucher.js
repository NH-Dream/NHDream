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

export { checkProductList,checkProductDetail }