import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useParams } from "react-router-dom"
import { useState,useEffect } from "react"
import { userStore } from "../../stores/userStore"      
import Swal from "sweetalert2"
import "@/assets/css/auto.css"
import Cleave from "cleave.js/react"
import { checkAutoDetail } from "@/services/auto"

export default function AutoModify1(){
  const navigate = useNavigate()
  const param = useParams()
  const id = param.id
  const [isOpenDate, setIsOpenDate] = useState(false)
  const [selectedDate, setSelectedDate] = useState("")
  const [amount,setAmount] = useState(null)
  const [endDate,setEndDate] =  useState('')
  

  const dates = [1,7,13,19,25,2,8,14,20,26,
  3 ,9,15,21,27,4,10,16,22,28,
  5,11,17,23,29,6,12,18,24,30]


  const dropdownDate = () => setIsOpenDate(!isOpenDate);
  

  const handleDateClick = (item) => {
    setSelectedDate(item)
    setIsOpenDate(false)
  };

  // 원래이체내역 정보가져오기
  useEffect(()=>{
    checkAutoDetail(
      id,
      res=>{
        // console.log(res)
        setAmount(res.amount)
        setEndDate(res.expiredDate)
        setSelectedDate(res.transferDay)
      },
      err=>{
        console.log(err)
      }
    )
  },[])

  const next = () => {
    if (amount && selectedDate && endDate){
      navigate(`/mypage/auto/modify2/${id}`,{ state: {  amount, selectedDate , endDate} })
    }else{
      Swal.fire({
        icon: "warning",
        html: "<b>이체금액,이체날짜,만료날짜를 모두 선택해주세요</b>",
        showCloseButton: true,
        showConfirmButton: false
      });
    }
  }
  
  
  const goCancel = () =>{
    navigate('/mypage/auto')
  }


  return(
    <div>
      <TopBar title="자동이체변경"/>
        <div className="autotransfer-container">       
          <div className="font-bold">이체금액</div>
          <div className="flex line mb-7">
            <Cleave 
                options={{
                    numeral: true,
                    numeralThousandsGroupStyle: 'thousand'
                }}
                className="input w-full h-8 focus:border-none focus:ring-0"
                value={amount}
                onChange={(e) => setAmount(e.target.rawValue)} 
                placeholder="이체금액 입력"
            />
            <div className="flex items-center font-bold text-lg">원</div>
          </div>
        <div className="font-bold">이체날짜(매월)</div>
        <div className="relative line mb-7">
          <div
              type="text"
              className="input w-full h-8 fake-input"
              readOnly
              value={selectedDate}
              onClick={dropdownDate}
              style={{ color:'gray' }}
          >
          {selectedDate ? `${selectedDate}일` : "이체날짜를 선택해주세요"}
          </div>
          {isOpenDate && (
              <ul className="dropdown-content menu p-2 bg-base-100 w-full absolute mt-1">
                  {dates.map((date, index) => (
                      <li key={index} onClick={() => handleDateClick(date)}>
                          <a>{date}</a>
                      </li>
                  ))}
              </ul>
          )}
        </div>
        <div className="font-bold">만료날짜</div>
        <div>(만료일은 이체날짜로 들어가게 됩니다)</div>
        <div className="flex line mb-7">
        <Cleave className="input w-full h-8 focus:border-none focus:ring-0"
          options={{date: true, delimiter: '-', datePattern: ['Y', 'm']}}
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)} />
        </div>
        </div>
      < TransferBottomBar 
          next={next} 
          cancelClick={goCancel}
          backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}