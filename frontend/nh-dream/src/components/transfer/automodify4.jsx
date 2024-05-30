import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate } from "react-router-dom"
import Check from "../../assets/images/mypage/check2.png"

export default function AutoModify4(){
  const navigate = useNavigate()
  

  const next = () =>{
    navigate('/mypage/auto')
  }

  return(
    <div>
      <div className="transfer-container">
        <div className="flex justify-center mb-5"><img src={Check} alt=""/></div>
        <div className="text-2xl text-center font-bold mb-10">자동이체변경 완료!</div>
      </div>
      < TransferBottomBar
      next={next} 
      showCancel={false} 
      buttonText="확인"
      backgroundColor="rgb(88, 193, 189)"/>
    </div>
  )
}