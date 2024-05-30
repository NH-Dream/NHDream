import { useNavigate } from "react-router-dom"
import { checkMyproduct } from "@/services/myproduct"
import { useEffect,useState } from "react"

export default function MydepositList(){
  const navigate = useNavigate()
  const [productList,setProductList] = useState([])
  
  useEffect(()=>{
    checkMyproduct(
      res=>{
        setProductList(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[])


  // 나중에 예금인지 적금인지 구분해서 deposit->saving으로 바꾸기
  // const goDetail = (id) =>{
  //   navigate(`/mypage/myaccount/${id}`)
  // }
  const goDepositHistory = (id) => {
    navigate(`/mypage/myaccount/deposit/history/${id}`);
  };

  const goSavingHistory = (id) => {
    navigate(`/mypage/myaccount/saving/history/${id}`);
  };
  

  return(
    <div className="p-3 pt-0">
      {productList.map((item,index) => (
        <div key={index} className="shadow-md rounded-xl p-3 mt-3" style={{ border: '2px solid #ccc' }}
        onClick={() => item.type === 1 ? goSavingHistory(item.accountId) : goDepositHistory(item.accountId)}>
          <div className="text-lg font-bold">{item.name}</div>
          <div className="text-lg ">{item.accountNum}</div>
          <div className="text-lg font-bold mt-10 flex justify-end">잔액 {item.balance.toLocaleString()}원</div>
        </div>
      ))}
    </div>
  )
}