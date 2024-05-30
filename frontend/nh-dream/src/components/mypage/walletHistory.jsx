import { userStore } from "../../stores/userStore"
import { useEffect, useState } from "react";
import { checkWalletHistory } from "../../services/wallet"
import copyImg from '@/assets/images/copy.png'
import Swal from "sweetalert2";

export default function WalletHistory(){
  const { walletAddress } = userStore()
  const [walletHistoryItems,setWalletHistoryItems] = useState([])


  // 거래내역조회 type이 1이라면 이체한것 출금으로 표현
  useEffect(()=>{
    checkWalletHistory(
      walletAddress,
      0,
      50,
      res=>{
        // console.log(res)
        setWalletHistoryItems(res)
        },
      err=>
      console.log(err)
    )
  },[])

  function formatAddress(address) {
    const visiblePart = address.slice(0, 25); 
    return `${visiblePart}...`;             
  }

  const moreBtn = (address) => {
    navigator.clipboard.writeText(address)
    Swal.fire({
      icon:'success',
      html: "<b>지갑주소가 복사되었습니다</b>",
      showCloseButton: true,
      showConfirmButton: false
    })
  }



  return(
    <div>
      {walletHistoryItems.map((item, index) => (
        <div key={index} className="p-5" style={{ borderBottom: '1px solid #ccc' }}>
          <div>{item.tradedAt.split('T')[0]}</div>
          <div className="flex">
            <div>{formatAddress(item.oppositeAccount)}</div>
            <div><img src={copyImg} alt="복사하기" onClick={()=>moreBtn(item.oppositeAccount)}/></div>
          </div>
            <div>{item.oppositeName}</div> 
          <div className="flex justify-end font-bold text-xl mt-5">
            <div className="mr-2">{item.type == 1 ? '출금':'입금'}</div>
            <div>
              <span style={{ color: item.type == 1 ? '#DC2626' : '#0284C7' }}>
              {item.tradeAmount.toLocaleString()}
              </span>
              원
            </div>
          </div>
          <div className="flex justify-end">잔액 {item.remainingBalance.toLocaleString()}원</div>
        </div>
      ))}
    </div>
  )
}