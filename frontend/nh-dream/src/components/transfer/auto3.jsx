import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation } from "react-router-dom"
import Swal from "sweetalert2"
import { useState } from "react"
import { sendAuto } from "@/services/auto"
import { userStore } from "@/stores/userStore"

export default function AutoTransfer3(){
  const navigate = useNavigate()
  const { walletAddress } = userStore()
  const { state } = useLocation()
  const address = state?.address
  const amount = state?.amount
  const period = state?.period
  const date = state?.date
  const [password,setPassword] = useState(null)

  const next = () =>{
    sendAuto(
      {
        recipientWalletAddress:address,
        recurringAmount:amount,
        senderWalletAddress:walletAddress,
        term:period,
        transferDay:date,
        walletPassword:password
      },
      res=>{
        // console.log(res)
        navigate('/auto/4')
      },
      err=>{
        console.log(err)
        Swal.fire({
          icon: "error",
          html: "<b>지갑비밀번호가 일치하지 않습니다</b>",
          showCloseButton: true,
          showConfirmButton: false
        })
      }
    ) 
  }

  return(
    <div>
      <TopBar title="자동이체등록"/>
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
      buttonText="자동이체등록하기"
      backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}