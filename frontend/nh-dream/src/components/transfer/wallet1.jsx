import TopBar from "../common/topBar"
import TransferBottomBar from "../common/transferBottomBar"
import "../../assets/css/app.css"
import { useNavigate } from "react-router-dom"
import { useState,useEffect } from "react"
import Swal from "sweetalert2"
import { checkWalletBalance } from "../../services/wallet"
import { recentTransferHistory } from "../../services/wallet"

export default function WalletTransfer1(){
  const navigate = useNavigate()
  const [walletBalance,setWalletBalance] = useState(null)
  const [address,setAddress] = useState("")
  const [amount,setAmount] = useState("")
  const [recentList,setRecentList] = useState([])
  const next = () =>{
    const numericAmount = parseFloat(amount.replace(/,/g, ""));
    if (!address || !amount) {
      Swal.fire({
        icon: "warning",
        html: "<b>지갑주소와 금액을 입력해주세요</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
    }
    else if (amount > walletBalance) {
      Swal.fire({
        icon: "error",
        html: "<b>금액이 지갑 잔액을 초과했습니다</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
    }
    else{
      navigate('/mypage/mywallet/2',{ state: { address,amount: numericAmount} })
    }
  }

  const goCancel = () =>{
    navigate('/mypage/mywallet')
  }

  useEffect(()=>{
    checkWalletBalance(
      res =>{
        setWalletBalance(res.balance)
        },
      err =>{console.log(err)}
    )
    recentTransferHistory(
      res => {
        const uniqueAddresses = new Set()
        const uniqueList = res.filter(item => {
          if (uniqueAddresses.has(item.address)) {
            return false
          } else {
            uniqueAddresses.add(item.address)
            return true
          }
        })
        setRecentList(uniqueList)
      },
      err => {
        console.log(err)
      }
    )
  },[])

  function formatAddress(address) {
    const visiblePart = address.slice(0, 18); 
    return `${visiblePart}...`;             
  }

  const autoInput = (input) =>{
    setAddress(input)
  }

  const handleAmountChange = (e) => {
    let value = e.target.value;
    value = value.replace(/,/g, ''); // Remove existing commas
    if (!isNaN(value) && value !== "") {
      const formatter = new Intl.NumberFormat('ko-KR');
      value = formatter.format(value);
    }
    setAmount(value);
  }


  return(
    <div>
      <TopBar />
      <div className="transfer-container">
        <div className="text-xl mb-5 text-center">누구에게 보낼까요?</div>
          <div className="mb-5 line">
            <input type="text" placeholder="지갑주소" 
            className="input w-full focus:border-none focus:ring-0"
            value={address}  onClick={()=>document.getElementById('recent').showModal()}
            onChange={(e)=>setAddress(e.target.value)}
           />
          </div>
          <div className="flex line mb-12">
            <input type="text" placeholder="보낼금액" 
            className="input w-full focus:border-none focus:ring-0"
            value={amount} onChange={handleAmountChange}
          />
            <div className="flex items-center font-bold text-lg">원</div>
          </div>
      </div>
      < TransferBottomBar 
      next={next} 
      cancelClick={goCancel}/>

      {/* 최근 거래기록 모달 */}
      <dialog id="recent" className="modal">
        <div className="modal-box" >
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
          </form>
          <div className="font-bold mb-3">보낼주소</div>
          <div className="line mb-7">
            <input type="text" placeholder="지갑주소" 
            className="input w-full h-8 focus:border-none focus:ring-0"
            value={address}
            onChange={(e)=>setAddress(e.target.value)}/>
          </div>
          <div>
            <span style={{ backgroundColor:'rgba(196, 239, 125, 0.2)'}} className="p-1 rounded-sm">최근입금지갑</span>
          </div>
          {recentList.length > 0 && recentList.map((item, index) => (
            <div
              key={index}
              className="p-4 px-0"
              style={{ borderBottom: "1px solid #ccc" }}
              onClick={() => autoInput(item.address)}
            >
              <div className="flex text-lg" style={{ color: "#848484" }}>
                <div className="mr-3">{item.name}</div>
                <div>{formatAddress(item.address)}</div>
              </div>
            </div>
          ))}
        <form method="dialog">
         <button className="btn w-full mt-5 text-lg" style={{ backgroundColor:'rgba(196, 239, 125, 0.8)'}} >확인</button>
        </form>
        </div>
      </dialog>
    </div>
  )
}