import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useParams,useLocation } from "react-router-dom"
import "@/assets/css/auto.css"


export default function AutoModify2(){
  const navigate = useNavigate()
  const param = useParams()
  const id = param.id
  const { state } = useLocation()
  const amount = state?.amount
  const date = state?.selectedDate
  const endDate = state?.endDate

  const next = () => {
    // console.log({ amount, date, fullEndDate })
    navigate(`/mypage/auto/modify3/${id}`,{ state: {  amount, date , fullEndDate} })
  }
  
  
  const goCancel = () =>{
    navigate('/mypage/auto')
  }
  
  
  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}`;
  }

  const formattedDate = date ? String(date).padStart(2, '0') : null;
  const fullEndDate = endDate && formattedDate ? `${endDate}-${formattedDate}` : endDate


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
      <TopBar title="자동이체변경"/>
        <div className="autotransfer-container">       
          <div className="font-bold">이체금액</div>
          <div className="flex">
            <div className="fake-input">
                <div className="ml-3">{formatCurrency(amount)}</div>
            </div>
            <div className="flex items-center font-bold text-lg">원</div>
          </div>
          <div className="ml-3 mb-4">(금{numberToKorean(amount)}원정)</div>
          <div className="font-bold">이체날짜(매월)</div>
          <div className="fake-input mb-4">
            <span className="ml-3">매월 {date}일</span>
          </div>
          <div className="font-bold">만료날짜</div>
          <div className="fake-input mb-4">
            <span className="ml-3">{fullEndDate}</span>
          </div>
        </div>
      < TransferBottomBar 
          next={next} 
          cancelClick={goCancel}
          backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}