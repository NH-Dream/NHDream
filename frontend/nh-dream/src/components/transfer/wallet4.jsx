import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation } from "react-router-dom"
import Check from "../../assets/images/mypage/check.png"

export default function WalletTransfer4(){
  const navigate = useNavigate()
  const { state } = useLocation()
  const remainBalance = state?.remainBalance
  const next = () =>{
    navigate('/mypage/mywallet')
  }

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}원`;
  }

  return(
    <div>
      <div className="transfer-container">
        <div className="flex justify-center mb-5"><img src={Check} alt=""/></div>
        <div className="text-2xl text-center font-bold mb-10">NHDC 송금완료!</div>
        <div className="text-center text-lg">송금후잔액 : {formatCurrency(remainBalance)}</div>
      </div>
      < TransferBottomBar 
      next={next} 
      showCancel={false} 
      buttonText="확인"/>
    </div>
  )
}