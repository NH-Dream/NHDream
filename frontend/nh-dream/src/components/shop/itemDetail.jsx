import { useParams,useNavigate } from "react-router-dom"
import { useState,useEffect } from "react"
import TopBar from "./topBar"
import "../../assets/css/shop.css"
import Delivery from "../../assets/images/shop/delivery.png"
import Seller from "../../assets/images/shop/seller.png"
import { checkProductDetail } from '@/services/voucher'

export default function ItemDetail(){
  const params = useParams()
  const itemId = params.itemId
  const [count,setCount] = useState(1)
  const navigate = useNavigate()
  const [item,setItem] = useState([])


  useEffect(()=>{
    checkProductDetail(
      itemId,
      res=>{
        // console.log(res)
        setItem(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[])




  const totalPrice = count * item.price

  const minus = () =>{
    setCount(preCount => Math.max(preCount-1,1))
  }

  const plus = () => {
    setCount(preCount => preCount + 1)
  }

  const handleOrder = () =>{
    navigate(`/shop/${itemId}/1`,{ state: { count, item, totalPrice } })
  }

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}`;
  }



  return(
    <div>
      <TopBar showBackIcon={true}/>
      <div className="flex justify-center">
        <img src={item.imageUrl} alt="" className="detail-size"/>
      </div>
      <div className="p-3">
        <h1 className="text-2xl font-bold mb-2">{item.title}</h1>
        <div className="flex">
          <img src={Delivery} alt="" />
          <h1 className="ml-2 flex items-center">CJ대한통운</h1>
          <div style={{
            borderRadius:'10px',
            border: '1px solid black'
          }}
          className="h-5 text-sm flex items-center mt-1 ml-2">
            <h1 className="p-1 text-xs">무료배송</h1>
            </div>
        </div>
        <div className="flex">
          <img src={Seller} alt="" />
          <h1 className="ml-2 flex items-center">판매자 : {item.affiliateName}</h1>
        </div>
      </div>
      <div className="flex p-3 pt-0">
        <div style={{
           flex: '1 1',
            marginRight:'10px',
            borderRadius:'5px',
            border: '2px solid black'
          }}
          className="h-10 text-2xl flex items-center justify-between">
          <h1 className="mb-1 ml-2" onClick={minus}>-</h1>
          <h1>{count}</h1>
          <h1 className="mb-1 mr-1" onClick={plus}>+</h1>
        </div>
        <div style={{
          flex: '1 1',
            borderRadius:'5px',
            border: '2px solid black'
          }}
          className="h-10 text-xl flex items-center justify-end font-bold">
            {formatCurrency(item.price*count)}
            <h1 className="mr-2 ml-2">드림</h1>
        </div>
      </div>
      <div className="p-3 pt-0">
        <button className="btn w-full text-lg bg-[#67D49D] text-white"
        onClick={handleOrder}>
        구매하기
        </button>
      </div>
      <div className="p-3 pt-0">
        <div className="font-bold text-lg pb-3">상품상세</div>
        <div style={{ 
          border:'2px solid #ccc',
          borderRadius:'5px'
        }} className="p-3 text-center">
          이 제품은 {item.affiliateName} 판매처의 상품으로<br /> 안심하고 믿고 사실수 있습니다 
        </div>         
        </div>
    </div>
  )
}