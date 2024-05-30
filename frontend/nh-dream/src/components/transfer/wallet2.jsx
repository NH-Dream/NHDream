import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation } from "react-router-dom"
import { useEffect,useState } from "react"
import { whoSending } from "../../services/wallet"
import Swal from "sweetalert2"

export default function WalletTransfer2(){
  
  const navigate = useNavigate()
  const { state } = useLocation()
  const address = state?.address
  const amount = state?.amount
  const [who,setWho] = useState(null)

  const next = () =>{
    navigate('/mypage/mywallet/3',{ state: { address,amount } })
  }

  const goCancel = () =>{
    navigate('/mypage/mywallet')
  }

  useEffect(()=>{
    whoSending(
      address,
      res=>{
        // console.log(res.oppositeName)
        setWho(res.oppositeName)
      },
      err=>{
        Swal.fire({
          icon: "error",
          html: "<b>존재하지 않는 지갑입니다</b>",
          showCloseButton: true,
          showConfirmButton: false
        })
        console.log(err)
        navigate('/mypage/mywallet/1')
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
      <TopBar />
      <div className="transfer-container2">
        <div className="text-xl mb-7 text-center">아래정보로 NHDC를 보낼까요?</div>
        <div className="mb-5">
          <div className="items-center font-bold text-lg mb-3">받는사람</div>
          <div className="fake-input mb-5 text-xl">{who}</div>
        </div>
        <div className="mb-5">
          <div className="items-center font-bold text-lg mb-3">지갑주소</div>
          <div className="fake-input mb-5" style={{ wordBreak: 'break-all' }}>{address}</div>
        </div>
          <div>
            <div className="items-center font-bold text-lg mb-3">보낼금액</div>
            <div className="flex">
              <div className="fake-input text-xl">
                {formatCurrency(amount)} 
              </div>
              <div className="flex items-center font-bold text-lg">원</div>
            </div>
            <div className="text-xl">(금{numberToKorean(amount)}원정)</div>
          </div>
      </div>
      < TransferBottomBar 
      next={next} 
      cancelClick={goCancel}/>
    </div>
  )
}