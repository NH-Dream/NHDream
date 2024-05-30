import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation } from "react-router-dom"
import { sendNhdc } from "../../services/wallet"
import Swal from "sweetalert2"
import { useState } from "react"

export default function WalletTransfer3(){
  const navigate = useNavigate()
  const { state } = useLocation()
  const address = state?.address
  const amount = state?.amount
  const [password,setPassword] = useState(null)
  

  const next = () =>{
    sendNhdc(
      {
        amount,
        recipientWalletAddress: address,
        password
      },
      res=>{
        // console.log(res)
        const remainBalance = res.remainingBalance
        navigate('/mypage/mywallet/4',{ state: { remainBalance } })
      },
      err => {
        console.log(err);
        console.log(err.response.data.dataHeader.resultCode);
        if (err.response.data.dataHeader.resultCode === 'T-004') {
          Swal.fire({
            icon: "error",
            html: "<b>자기자신에게 전송은 허용되지 않습니다</b>",
            showCloseButton: true,
            showConfirmButton: false
          });
        } else {
          Swal.fire({
            icon: "error",
            html: "<b>지갑비밀번호가 일치하지 않습니다</b>",
            showCloseButton: true,
            showConfirmButton: false
          });
        }
      }     
    );  
  }

  return(
    <div>
      <TopBar />
      <div className="transfer-container">
        <div className="text-xl text-center">지갑비밀번호를 입력하세요</div>
        <div className="text-center mb-5">(6자리 숫자)</div>
          <div className="line">
            <input type="password" placeholder="비밀번호" 
            className="input w-full focus:border-none focus:ring-0"
            onChange={(e)=>setPassword(e.target.value)} />
          </div>
      </div>
      < TransferBottomBar 
      next={next} 
      showCancel={false} 
      buttonText="보내기"/>
    </div>
  )
}