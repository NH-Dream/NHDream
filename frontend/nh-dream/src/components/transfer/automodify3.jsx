import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate,useLocation,useParams } from "react-router-dom"
import Swal from "sweetalert2"
import { useState } from "react"
import { modifyAutoTransfer } from "@/services/auto"
import { confrimWalletPassword } from "@/services/wallet"


export default function AutoModify3(){
  const navigate = useNavigate()
  const param = useParams()
  const id = param.id
  const { state } = useLocation()
  const amount = state?.amount
  const date = state?.date
  const fullEndDate = state?.fullEndDate
  const [password,setPassword] = useState(null)

  const next = () =>{
    confrimWalletPassword(
      {
      walletPassword :password
      },
      res=>{
        // console.log(res)
        modifyAutoTransfer(
          {
            amount,
            transferDate:date,
            expiredAt:fullEndDate
          },
          id,
          res=>{
            // console.log(res)
            navigate(`/mypage/auto/modify4/${id}`)
          },
          err=>{
            console.log(err)
          }
        )
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
      <TopBar title="자동이체변경"/>
      <div className="transfer-container">
        <div className="text-xl text-center mb-5">비밀번호를 입력하세요</div>
          <div className="line">
            <input type="password" placeholder="비밀번호" 
            className="input w-full focus:border-none focus:ring-0"
            onChange={(e)=>setPassword(e.target.value)} />
          </div>
      </div>
      < TransferBottomBar
      next={next} 
      showCancel={false} 
      buttonText="자동이체변경하기"
      backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}