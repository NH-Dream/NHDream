import TopBar from "../common/topBar"
import Wallet from "../../assets/images/mypage/wallet2.png"
import Setting from "../../assets/images/mypage/setting2.png"
import WalletHistory from "./walletHistory"
import { useNavigate } from "react-router-dom"
import { userStore } from "../../stores/userStore"
import { checkWalletBalance } from '../../services/wallet'
import { useEffect, useState } from "react"
import copyImg from '@/assets/images/copy.png'
import Swal from "sweetalert2"

export default function MyWallet() {
  const navigate = useNavigate()
  const { userName } = userStore()
  const { walletAddress } = userStore()
  const [walletBalance, setWalletBalance] = useState(null)

  const moveChangePassword = () => {
    navigate('/mypage/mywallet/changePassword')
  }
  const goTransfer = () => {
    navigate('/mypage/mywallet/1')
  }
  function formatAddress(address) {
    const visiblePart = address.slice(0, 33); 
    return `${visiblePart}...`;             
  }
  const moreBtn = () => {
    navigator.clipboard.writeText(walletAddress)
    Swal.fire({
      icon: 'success',
      html: "<b>나의 지갑주소가 복사되었습니다</b>",
      showCloseButton: true,
      showConfirmButton: false
    })
  }
  useEffect(() => {
    checkWalletBalance(
      res => {
        // console.log(res)
        setWalletBalance(res.balance)
      },
      err => { console.log(err) }
    )
  }, [])

  const formattedWalletBalance = walletBalance ? `${walletBalance.toLocaleString()}원` : '';

  return (
    <div>
      <TopBar title="마이지갑" color="rgba(196, 239, 125, 0.5)" />
      <div className='h-66 p-5' style={{ backgroundColor: "rgba(196, 239, 125, 0.5)" }}>
        <div className="flex justify-between">
          <div className="flex">
            <img src={Wallet} alt="" />
            <div className='flex text-xl ml-2 mt-1.5'><h1 className='font-bold'>{userName}</h1>님의 NHDC지갑</div>
          </div>
          <div onClick={() => moveChangePassword()} className="flex items-center"><img src={Setting} alt="비밀번호 변경" /></div>
        </div>
        <div className="pt-2 text-sm flex">
          <div className="mr-1">{formatAddress(walletAddress)}</div>
          <div><img src={copyImg} alt="복사하기" onClick={moreBtn} /></div>
        </div>
        <div className="flex justify-end font-bold text-2xl pt-8 mr-5">{formattedWalletBalance}</div>
        <div className="flex w-full pt-8" onClick={goTransfer}>
          <button className="btn flex w-full text-lg">보내기</button>
        </div>
      </div>
      <div className="p-3 text-lg font-bold" style={{ borderBottom: '1px solid #ccc' }}>
        최근거래내역
      </div>
      <WalletHistory />
    </div>
  )
}