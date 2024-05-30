import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate } from "react-router-dom"
import { useState,useEffect } from "react"
import { userStore } from "../../stores/userStore"      
import Swal from "sweetalert2"
import "@/assets/css/auto.css"
import { recentTransferHistory } from "../../services/wallet"

export default function AutoTransfer1(){
  const navigate = useNavigate()
  const { walletAddress } = userStore()
  const [isOpenDate, setIsOpenDate] = useState(false)
  const [selectedPeriod, setSelectedPeriod] = useState("")
  const [selectedDate, setSelectedDate] = useState("")
  const [address,setAddress] = useState("")
  const [amount,setAmount] = useState("")
  const [recentList,setRecentList] = useState([])


  const dates = [1,7,13,19,25,2,8,14,20,26,
  3 ,9,15,21,27,4,10,16,22,28,
  5,11,17,23,29,6,12,18,24,30]


  const dropdownDate = () => setIsOpenDate(!isOpenDate);



  const handleDateClick = (item) => {
    setSelectedDate(item);
    setIsOpenDate(false)
  };


  const next = () => {
    const numericAmount = parseFloat(amount.replace(/,/g, ""));
    if (!address || !amount) {
      Swal.fire({
        icon: "warning",
        html: "<b>지갑주소와 금액을 입력해주세요</b>",
        showCloseButton: true,
        showConfirmButton: false
      });
    } 
    else if (!selectedPeriod || !selectedDate) {
      Swal.fire({
        icon: "warning",
        html: "<b>이체기간과 이체날짜를 모두 선택해주세요</b>",
        showCloseButton: true,
        showConfirmButton: false
      });
    } 
    else {
      navigate('/auto/2', { state: { address, amount:numericAmount, selectedPeriod, selectedDate } });
    }
  }

  const handlePeriodChange = (value) => {
    const numericValue = parseInt(value, 10); // 입력값을 숫자로 변환
    if (numericValue <= 200) {
      setSelectedPeriod(numericValue);
    } else {
      Swal.fire({
        icon: "error",
        html: "<b>이체기간은 200개월을 초과할수 없습니다</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
      // 선택적으로 잘못된 값을 입력 필드에서 제거
      setSelectedPeriod(''); // 입력 필드를 비움
    }
  }
  
  
  const goCancel = () =>{
    navigate('/mypage/auto')
  }

  useEffect(()=>{
    recentTransferHistory(
      res => {
        const uniqueAddresses = new Set()
        const uniqueList = res.filter(item => {
          if (uniqueAddresses.has(item.address)) {
            return false
          } else {
            uniqueAddresses.add(item.address)
            return true
          }
        })
        setRecentList(uniqueList)
      },
      err => {
        console.log(err)
      }
    )
  })

  function formatAddress(address) {
    const visiblePart = address.slice(0, 18); 
    return `${visiblePart}...`;             
  }

  const autoInput = (input) =>{
    setAddress(input)
  }


  const handleAmountChange = (e) => {
    let value = e.target.value;
    value = value.replace(/,/g, ''); // Remove existing commas
    if (!isNaN(value) && value !== "") {
      const formatter = new Intl.NumberFormat('ko-KR');
      value = formatter.format(value);
    }
    setAmount(value);
  }


  return(
    <div>
      <TopBar title="자동이체등록"/>
        <div className="autotransfer-container">       
          <div className="font-bold">보낼주소</div>
          <div className="line mb-7">
            <input type="text" placeholder="지갑주소" 
            className="input w-full h-8 focus:border-none focus:ring-0"
            value={address} onClick={()=>document.getElementById('recent').showModal()}
            onChange={(e)=>setAddress(e.target.value)}
            />
          </div>
          <div className="font-bold">이체금액</div>
          <div className="flex line mb-7">
            <input type="type" placeholder="이체금액" 
            className="input w-full h-8 focus:border-none focus:ring-0" 
            value={amount} onChange={handleAmountChange}/>
            <div className="flex items-center font-bold text-lg">원</div>
          </div>
          <div className="font-bold">출금지갑</div>
          <div className="fake-input mb-7">
            <span className="wallet-address ml-3" style={{ color:'gray' }}>{ walletAddress }</span>
          </div>
          <div className="font-bold">이체주기</div>
          <div className="fake-input mb-7">
              <span className="ml-3" style={{ color:'gray' }}>매1개월</span>
          </div>
          <div className="font-bold">이체기간(개월)</div>
          <div className="flex line mb-7">
            <input type="number" placeholder="이체기간" 
            className="input w-full h-8 focus:border-none focus:ring-0" 
            onChange={(e)=>(handlePeriodChange(e.target.value))}
            max="200"/>
          </div>        
        <div className="font-bold">이체날짜(매월)</div>
        <div className="relative line mb-2">
          <div
              type="text"
              className="input w-full h-8 fake-input"
              readOnly
              onClick={dropdownDate}
              style={{ color:'gray' }}
          >
          {selectedDate ? `${selectedDate}일` : "이체날짜를 선택해주세요"}
          </div>
          {isOpenDate && (
              <ul className="dropdown-content2 menu p-2 bg-base-100 w-full absolute mt-1">
                  {dates.map((date, index) => (
                      <li key={index} onClick={() => handleDateClick(date)}>
                          <a>{date}</a>
                      </li>
                  ))}
              </ul>
          )}
        </div> 
        </div>
      < TransferBottomBar 
          next={next} 
          cancelClick={goCancel}
          backgroundColor="rgb(88, 193, 189)"/>

      {/* 최근 거래기록 모달 */}
      <dialog id="recent" className="modal">
        <div className="modal-box" >
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
          </form>
          <div className="font-bold mb-3">보낼주소</div>
          <div className="line mb-7">
            <input type="text" placeholder="지갑주소" 
            className="input w-full h-8 focus:border-none focus:ring-0"
            value={address}
            onChange={(e)=>setAddress(e.target.value)}/>
          </div>
          <div>
            <span style={{ backgroundColor:'rgb(88, 193, 189,0.2)'}} className="p-1 rounded-sm">최근입금지갑</span>
          </div>
          { recentList.length > 0 &&  recentList.map((item, index) => (
          <div key={index} className="p-4 px-0" style={{ borderBottom: '1px solid #ccc' }}
          onClick={()=>autoInput(item.address)}>
            <div className="flex text-lg" style={{ color:'#848484' }}>
              <div className="mr-3">{item.name}</div>
              <div>{formatAddress(item.address)}</div>
            </div>
          </div>
          ))}
        <form method="dialog">
         <button className="btn w-full mt-5 text-lg" style={{ backgroundColor:'rgb(88, 193, 189)'}} >확인</button>
        </form>
        </div>
      </dialog>
    </div>
  )
}