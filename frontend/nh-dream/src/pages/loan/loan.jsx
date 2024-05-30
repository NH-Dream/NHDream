import TopBar from "../../components/common/topBar"
import LoanImage from "../../assets/images/loan/loan.png"
import { LoanListComponent } from "../../components/loan/index"
import { useNavigate } from "react-router-dom"

export default function LoanPage(){
  const navigate = useNavigate()

  return(
    <div>
      <TopBar title="대출" showBackIcon={false} />
      <div className="p-3"  style={{ borderTop: '2px solid #ccc' }}> 
        <div className="p-3 rounded-lg shadow-md mb-5"
          style={{
            background: "rgb(46,177,173)",
            minHeight: '250px'
          }}
          onClick={()=>navigate('/loan/cal')}>
            <div className="text-white font-bold text-2xl text-center">누구나 간단하고 빠르게</div>
            <div className="text-white font-bold text-2xl text-center">대출 납입 계산하기</div>
            <img src={LoanImage} alt="" style={{ width:180,height:130}} />
            <div className="text-white text-lg text-end">대출금액 / 기간 / 금리 입력</div>
            <div className="text-white text-lg text-end">내야하는 금액 간편 확인</div>
        </div>
        <LoanListComponent />
      </div>
    </div>
  )
}