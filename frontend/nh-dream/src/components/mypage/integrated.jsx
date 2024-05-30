import { useEffect, useState } from "react"
import { userStore } from "@/stores/userStore"
import { allTransactionList } from "@/services/user"
import TopBar from "../common/topBar"
import copyImg from '@/assets/images/copy.png'
import Swal from "sweetalert2"

export default function Integrated() {
  const store = userStore()
  const [selected, setSelected] = useState('total')
  const [transferItems, setTransferItems] = useState([])

  const filteredItems = transferItems.filter(item => {
    if (selected === 'total') return true; // 전체 탭은 모든 항목을 표시
    return item.transactionType === (selected === 'deposit' ? 0 : 1);
  })

  const moreBtn = (accountNum) => {
    navigator.clipboard.writeText(accountNum)
    Swal.fire({
      title:"복사되었습니다.",
      confirmButtonColor: "#1E7572",
      confirmButtonText: "확인",
    })
  }

  const plusBar = (accountNum) => {
    if (accountNum[1] == 'x') {
      if (accountNum.length >= 30) {
        return accountNum.slice(0,30) + '...'
      }
    } else {
      if (accountNum.length >= 6) {
          return accountNum.slice(0, 3) + '-' + accountNum.slice(3, 5) + '-' + accountNum.slice(5);
      } else if (accountNum.length >= 4) {
          return accountNum.slice(0, 3) + '-' + accountNum.slice(3);
      }
    }
}

const removeT = (tansactionDate) => {
  return tansactionDate.slice(0,10) + " " + tansactionDate.slice(11,16)
}

  useEffect(() => {
    allTransactionList(
      store.userId,
      res => {
        setTransferItems(res.transactionDtos)
      },
      err => {
        console.log("통합목록불러오기 안됩니다.", err)
      }
    )
  }, [])

  return (
    <div>
      <TopBar title="통합거래내역조회" color="#2EB1AD33" />
      <div className="font-bold text-xl p-3 mb-5" style={{ backgroundColor: "#2EB1AD33" }}>
        거래내역
      </div>
      <div className="flex px-5" style={{ borderBottom: '2px solid #ccc' }}>
        <div
          className='flex flex-col justify-end mr-7 font-bold'
          onClick={() => setSelected('total')}
        >
          <div className="text-xl">전체</div>
          <div className="h-1" style={{
            backgroundColor: selected === 'total' ? '#2EB1AD' : 'transparent',
            borderRadius: '15px 15px 0 0'
          }}></div>
        </div>
        <div
         className='flex flex-col justify-end mr-7 font-bold'
          onClick={() => setSelected('deposit')}
        >
          <div className="text-xl">입금</div>
          <div className="h-1" style={{
            backgroundColor: selected === 'deposit' ? '#2EB1AD' : 'transparent',
            borderRadius: '15px 15px 0 0'
          }}></div>
        </div>
        <div
          className='flex flex-col justify-end mr-7 font-bold'
          onClick={() => setSelected('withdraw')}
        >
          <div className="text-xl">출금</div>
          <div className="h-1" style={{
            backgroundColor: selected === 'withdraw' ? '#2EB1AD' : 'transparent',
            borderRadius: '15px 15px 0 0'
          }}></div>
        </div>
      </div>
      {filteredItems.map((item, index) => (
        <div key={index} className="p-5" style={{ borderBottom: '1px solid #ccc' }}>
          <div>{removeT(item.tansactionDate)}</div>
          {item.accountType == '자유예금' ? (<div>지갑이체</div>) : (<div>{item.accountType}</div>)}
          {(item.accountNum).length >= 25 ?
          (<div className="font-bold flex">
            <div>{plusBar(item.accountNum)}</div>
            <div><img src={copyImg} alt="복사하기" onClick={() => moreBtn(item.accountNum)}/></div>
          </div>) :
          (<div className="font-bold">{plusBar(item.accountNum)}</div>)
          }
          <div>{item.productName}</div>
          <div className="flex justify-end font-bold text-xl">
            {item.transactionType == 0 &&
              <div className="font-bold text-xl">입금 <span className="text-sky-600">{item.amount.toLocaleString()}</span>원</div>
            }
            {item.transactionType == 1 &&
              <div className="font-bold text-xl">출금 <span className="text-red-600">{item.amount.toLocaleString()}</span>원</div>
            }
          </div>
          <div className="flex justify-end">잔액 {item.outstanding.toLocaleString()}원</div>
        </div>
      ))}
    </div>
  )
}