import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation } from "react-router-dom"
import { useEffect,useState } from "react"
import { whoSendingDate } from "../../services/wallet"
import Swal from "sweetalert2"
import { userStore } from "../../stores/userStore"  
import "@/assets/css/auto.css"

export default function AutoTransfer2(){
  const { walletAddress } = userStore()
  const navigate = useNavigate()
  const { state } = useLocation()
  const address = state?.address
  const amount = state?.amount
  const period = state?.selectedPeriod
  const date = state?.selectedDate
  const [who,setWho] = useState(null)
  const [transStartday,setTransStartday] = useState(null)

  const next = () =>{
    navigate('/auto/3',{ state: { address,amount,period,date } })
  }

  const goCancel = () =>{
    navigate('/mypage/auto')
  }

  useEffect(()=>{
    whoSendingDate(
      address,
      date,
      res=>{
        // console.log(res)
        setWho(res.oppositeName)
        setTransStartday(res.startDate)
      },
      err=>{
        console.log(err)
        Swal.fire({
          icon: "error",
          html: "<b>존재하지 않는 지갑입니다</b>",
          showCloseButton: true,
          showConfirmButton: false
        })    
        navigate('/auto/1')
      }
    )
  },[])

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}`;
  }

   // 숫자를 한글로 변환하는 함수
   function numberToKorean(amount) {
    const units = ["", "만", "억", "조", "경"];
    const smallUnits = ["", "십", "백", "천"];
    const digits = ["", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"];
  
    function splitNumber(num) {
      const numStr = num.toString();
      const splitNumbers = [];
      for (let i = numStr.length; i > 0; i -= 4) {
        splitNumbers.unshift(numStr.substring(i - 4, i));
      }
      return splitNumbers.map(str => parseInt(str, 10));
    }
  
    function convertSmallNumber(num) {
      let result = "";
      const numStr = num.toString();
      for (let i = 0; i < numStr.length; i++) {
        const digit = numStr[numStr.length - 1 - i];
        if (digit !== '0') {
          result = digits[parseInt(digit)] + smallUnits[i] + result;
        }
      }
      return result.replace(/일백/g, "백").replace(/일천/g, "천").replace(/일십/g, "십");
    }
  
    const splitNumbers = splitNumber(amount);
    let result = "";
    for (let i = 0; i < splitNumbers.length; i++) {
      if (splitNumbers[i] !== 0) {
        result += convertSmallNumber(splitNumbers[i]) + units[splitNumbers.length - 1 - i];
      }
    }
    return result;
  }

  return(
    <div>
      <TopBar title="자동이체등록"/>
      <div className="autotransfer-container">
        <div className="text-xl text-center">아래정보로 자동이체를</div>
        <div className="text-xl mb-5 text-center">등록할까요?</div>
        <div className="font-bold">받는분</div>
        <div className="fake-input mb-4">
          <span className="ml-3">{who}</span>
        </div>
        <div className="font-bold">보낼주소</div>
        <div className="fake-input mb-4">
          <span className="wallet-address ml-3">{address}</span>
        </div>
        <div className="font-bold">이체금액</div>
        <div className="flex">
          <div className="fake-input">
            <span className="ml-3">{formatCurrency(amount)} </span>
          </div>
          <div className="flex items-center font-bold text-lg">원</div>
        </div>
        <div className="mb-4 ml-3">(금{numberToKorean(amount)}원정)</div>
        <div className="font-bold">출금지갑</div>
        <div className="fake-input mb-4">
          <span className="wallet-address ml-3">{ walletAddress }</span>
        </div>
        <div className="font-bold"> 이체기간</div>
        <div className="fake-input mb-4">
          <span className="ml-3">{period}개월</span>
        </div>
        <div className="font-bold"> 이체주기</div>
        <div className="fake-input mb-4">
          <span className="ml-3">매1개월</span>
        </div>
        <div className="font-bold">이체날짜</div>
        <div className="fake-input mb-4">
          <span className="ml-3">매월 {date}일</span>
        </div>
        <div className="font-bold">이체시작일</div>
        <div className="fake-input mb-4">
          <span className="ml-3">{transStartday}</span>
        </div>
      </div>
      < TransferBottomBar 
        next={next} 
        cancelClick={goCancel}
        backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}