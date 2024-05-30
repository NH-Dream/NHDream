import TopBar from "@components/common/topBar"
import Setting from "@/assets/images/mypage/setting3.png"
import { useNavigate,useParams } from "react-router-dom"
import { useEffect,useState } from "react"
import { checkSavingHistory } from "@/services/myproduct"

export default function MySavingHistory(){
  const param = useParams()
  const id = param.id
  const navigate = useNavigate()
  const [savingName,setsavingName] = useState(null)
  const [accountNum,setAccountNum] = useState(null)
  const [balance,setBalance] = useState(null)
  const [productHistoryItems,setProductHistoryItems] = useState([])


  const goInformation = () =>{
    navigate(`/mypage/myaccount/saving/${id}`)
  }

  useEffect(()=>{
    checkSavingHistory(
      id,
      0,
      50,
      res=>{
        console.log(res)
        setsavingName(res.name)
        setAccountNum(res.accountNum)
        setBalance(res.balance)
        setProductHistoryItems(res.savingTransactionList)
      },
      err=>{
        console.log(err)
      }
    )
  },[])

  const formattedBalance = balance ? `${balance.toLocaleString()}원` : '0원'

  return(
    <div>
      <TopBar color="#C8F0C5"/>
      <div className='h-52 p-5' style={{ backgroundColor:"#C8F0C5" }}>
        <div className="flex justify-between">
          <div className='flex text-xl font-bold'>{savingName}</div>
          <div style={{ width: '40px', height: '40px', display: 'flex', justifyContent: 'center', alignItems: 'center', cursor: 'pointer' }}
              onClick={goInformation}>
            <img src={Setting} alt="" style={{ height: 20 }} />
          </div>
        </div>
        <div className="text-lg">{accountNum}</div>
        <div className="flex justify-end font-bold text-2xl pt-8">{formattedBalance}</div>
      </div>
      <div className="p-3 text-lg font-bold" style={{ borderBottom: '1px solid #ccc'}}>
        자동이체내역
      </div>
      {productHistoryItems.map((item, index) => (
        <div key={index} className="p-5" style={{ borderBottom: '1px solid #ccc' }}>
          <div className="flex justify-between">
            <div>{item.tradedAt}</div>
          </div>
          <div>{item.oppositeName}</div>
          <div className="flex justify-end font-bold text-xl mt-5">
            입금 {item.tradeAmount.toLocaleString()}원
          </div>
          <div className="flex justify-end">잔액 {item.remainingBalance.toLocaleString()}원</div>
        </div>
      ))}
    </div>
  )
}