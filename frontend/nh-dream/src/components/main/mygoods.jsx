import { useNavigate } from "react-router-dom"
import { checkMyproduct } from "@/services/myproduct"
import { useEffect,useState } from "react"
import Saving from "@/assets/images/main/saving.png"
import Pig from "@/assets/images/main/pinkpig.png"


export default function MyGoods(){
  const navigate = useNavigate()
  const [myGoodsItems,setMyGoodsItems] = useState([])

  useEffect(()=>{
    checkMyproduct(
      res=>{
        // console.log(res)
        setMyGoodsItems(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[])


  const goDepositeDetail = (id) =>{
    navigate(`/mypage/myaccount/deposit/history/${id}`)
  }

  const goSavingDetail = (id) =>{
    navigate(`/mypage/myaccount/saving/history/${id}`)
  }


  return(
    <div className="p-3 pt-0">
    {myGoodsItems.length > 0 ? (
      myGoodsItems.map((item, index) => (
        <div key={index} className="p-3 shadow-md rounded-xl h-20"
          onClick={() => item.type === 1 ? goSavingDetail(item.accountId) : goDepositeDetail(item.accountId)}>
          <div>
            <span className="text-sm p-1" 
              style={{ 
                backgroundColor: item.type === 1 ? 
                'rgba(196, 239, 125, 0.5)' : 'rgba(249, 248, 113, 0.5)'
              }}>
              {item.type === 1 ? '적금상품' : '예금상품'}
            </span>
          </div>
          <div className="text-lg mt-1 font-bold">{item.name}</div>
        </div>
      ))
    ) : (
      <div className="px-3">
        <div className="text-sm pb-5">아직 가입한 상품이 없습니다.</div>
        <div className="shadow-md flex justify-around rounded-xl" style={{ backgroundColor:'rgba(153, 226, 242, 0.3)' }}
        onClick={()=>navigate('/saving')}>
          <img src={Pig} alt="예금" style={{ width:160,height:100}}/>
          <div className="text-lg flex items-center mr-5">예금상품보러가기</div>
        </div>
        <div className="shadow-md flex justify-around mt-5 rounded-xl" style={{ backgroundColor: 'rgba(242, 182, 204, 0.3)' }}
        onClick={()=>navigate('/saving?saving=true')}>
          <div className="text-lg flex items-center ml-5">적금상품보러가기</div>
          <img src={Saving} alt="적금" style={{ width:150,height:100}}/>
        </div>
      </div>
    )}
  </div>
  )
}