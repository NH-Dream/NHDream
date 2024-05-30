import "../../assets/css/shop.css"
import { useNavigate } from "react-router-dom"
import { useEffect,useState } from "react"
import { checkProductList } from "@/services/voucher"

export default function biryoList(){
  const navigate = useNavigate()
  const [itemList,setItemList] = useState([])

  useEffect(()=>{
    checkProductList(
      1,
      res=>{
        // console.log('비료조회완료')
        setItemList(res)
       
      },
      err=>console.log(err)
    )
  },[])

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}드림`;
  }



  const goDetail = (itemId) =>{
    navigate(`/shop/${itemId}`)
  }

  return(
    <div className="grid grid-cols-2">
       {itemList.map(item => (
        <div key={item.id} className="p-3"
        onClick={()=>goDetail(item.id)}>
          <div className="p-2 shadow-sm"
          style={{
            borderWidth: '2px',
            borderStyle: 'solid',
            backgroundColor: 'white',
            borderRadius: '5px'
          }}>
            <div className="p-2 flex justify-center">
              <img src={item.imageUrl} alt="" className="size" />
            </div>
            <div className="flex flex-col items-center justify-center">
              <span className="text-md font-bold">{item.title}</span>
              <span className="font-bold" style={{ color: '#1E7572' }}>{formatCurrency(item.price)}</span>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}