import TopBar from "./topBar"
import "../../assets/css/shop.css"
import { useLocation,useNavigate,useParams } from "react-router-dom"
import { userStore } from "@/stores/userStore"

export default function Order2(){
  const { userName } = userStore()
  const { userPhone } = userStore()
  const location = useLocation()
  const navigate = useNavigate()
  const params = useParams()
  const itemId = params.itemId
  const { count, item, totalPrice,usePoint } = location.state 

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}원`;
  }


  return(
    <div>
      <TopBar />
      <div className="p-2">
        <div style={{ fontWeight:900}} className="text-xl mb-2">주문이 완료되었습니다!</div>
        <div className="shadow-md" style={{ border: '1px solid #ccc' }}>
          <div className="flex p-2">
            <h1 style={{ flex : 3}} className="title1">받는사람</h1> 
            <h1 style={{ flex : 7}} className="content1">{ userName }</h1>
          </div>
          <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">연락처</h1> 
          <h1 style={{ flex : 7}} className="content1">{ userPhone }</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">배송지</h1> 
          <h1 style={{ flex : 7}} className="content1">경상북도 구미시 진평동</h1>
        </div>
        <div className="p-2">
          <div style={{  borderBottom: '1px solid gray' }} className=""></div>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">상품명</h1> 
          <h1 style={{ flex : 7}} className="content1">{item.title}</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">총수량</h1> 
          <h1 style={{ flex : 7}} className="content1">{count}개</h1>
        </div>
        <div className="p-2">
          <div style={{  borderBottom: '1px solid gray' }} className=""></div>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 5}} className="title1">총 상품금액</h1> 
          <h1 style={{ flex : 5}} className="content1">{formatCurrency(totalPrice)}</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 5}} className="title1">배송비</h1> 
          <h1 style={{ flex : 5}} className="content1">0원</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 5}} className="title1">사용드림포인트</h1> 
          <h1 style={{ flex : 5}} className="content1">{formatCurrency(usePoint)}</h1>
        </div>
        <div className="p-2">
          <div style={{  borderBottom: '1px solid gray' }} className=""></div>
        </div>
        <div className="flex p-2">
          <div className="content1 text-xl" style={{ flex : 5}} >최종결제금액</div>
          <div className="content1 text-red-500" style={{ flex : 5}} >{formatCurrency(totalPrice-usePoint)}</div>
        </div>
        </div>
      </div>
      <div className="flex p-2 gap-2">
        <button className="btn text-lg flex-1 bg-[white] text-[#67D49D] border-[#67D49D] hover:text-black hover:bg-white"
          onClick={()=>navigate(`/shop/${itemId}`)}>
          상품보기
        </button>
        <button className="btn text-lg flex-1 bg-[#67D49D] text-white hover:text-black hover:bg-[#67D49D]"
          onClick={()=>navigate('/shop')}>
          쇼핑계속하기
        </button>
      </div>
    </div>
  )
}