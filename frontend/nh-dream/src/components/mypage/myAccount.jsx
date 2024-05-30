import { useState } from "react"
import TopBar from "../common/topBar"
import Account from "../../assets/images/mypage/account2.png"
import DepositList from "./depositList"
import LoanList from "./loanList"
import { userStore } from "../../stores/userStore"

export default function MyAccount(){
  const [selected,setSelected] = useState('deposit')
  const { userName } = userStore()
  const renderComponent = () => {
    switch (selected) {
      case 'deposit':
        return <DepositList />;
      case 'loan':
        return <LoanList />;
    }
  };

  return(
    <div>
      <TopBar title="마이계좌" color="#2EB1AD33"/>
      <div className='h-24 p-5 flex' style={{ backgroundColor:"#2EB1AD33" }}>
        <div><img src={Account} alt="" /></div>
        <div className='flex text-xl ml-2'><h1 className='font-bold'>{userName}</h1>님의 계좌</div>
      </div>
      <div className="p-3 pt-2">
        <div className="h-12 flex justify-between" style={{ borderBottom: '2px solid #ccc' }}>
        <div className="flex flex-1 justify-center items-end" onClick={() => setSelected('deposit')}>
          <div className="flex flex-col justify-end">
            <div className="font-bold text-xl">예/적금</div>
            <div className="h-1.5" style={{
                backgroundColor: selected === 'deposit' ? '#2EB1AD' : 'transparent',
                borderRadius: '15px 15px 0 0'
              }}></div>
          </div>
        </div>
        <div className="flex flex-1 justify-center items-end" onClick={() => setSelected('loan')}>
          <div className="flex flex-col justify-end">
            <div className="font-bold text-xl">대출</div>
            <div className="h-1.5" style={{
                backgroundColor: selected === 'loan' ? '#2EB1AD' : 'transparent',
                borderRadius: '15px 15px 0 0'
              }}></div>
            </div>
        </div>
        </div>
      </div>
      {renderComponent()}
    </div>
  )
}