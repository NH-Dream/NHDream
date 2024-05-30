import TopBar from "../common/topBar"
import { useState } from "react"
import Swal from "sweetalert2"
// import calcLoanInterest from 'calc-loan-interest';

export default function Calculator(){
  const [loanAmount,setLoanAmount] = useState(null) // 대출금액
  const [loanPeriod,setloanPeriod] = useState(null) // 대출기간
  const [loanRate,setLoanRate] = useState(null) // 대출금리
  const [totalPayment, setTotalPayment] = useState(0) // 총내야하는금액
  const [monthlyPayment, setMonthlyPayment] = useState(0) //매월내야하는금액
  const [totalInterest, setTotalInterest] = useState(0) //총이자

  // const calculateLoan2 = () => {
  //   const main = calcLoanInterest(
  //     0,
  //     loanAmount,
  //     loanRate,
  //     loanPeriod,
  //     0,
  //   )
  //   if (main) {
  //     setTotalPayment(Math.floor(main["totalInterest"]))
  //     setTotalInterest(Math.floor(main["totalRepay"]))
  //     setMonthlyPayment(Math.floor(main["totalInterest"]/12))
  //   }
  // }


  const calculateLoan = () => {
    const principal = parseFloat(loanAmount);
    const months = parseFloat(loanPeriod);
    const annualRate = parseFloat(loanRate);
    const monthlyRate = annualRate / 100 / 12;

    if (!isNaN(principal) && !isNaN(months) && !isNaN(annualRate) && months > 0 && annualRate > 0) {
      const monthly = principal * (monthlyRate * (1 + monthlyRate) ** months) / ((1 + monthlyRate) ** months - 1);
      const total = monthly * months;
      const interest = total - principal;

      setMonthlyPayment(Math.floor(monthly).toLocaleString());
      setTotalPayment(Math.floor(total).toLocaleString());
      setTotalInterest(Math.floor(interest).toLocaleString());
    } else {
      Swal.fire({
        icon: "warning",
        html: `
        <b>모든 입력 필드에 유효한 숫자를 입력해 주세요</b>
        `,
        showCloseButton: true,
        showConfirmButton: false  
      });
    }
  }

  return(
    <div>
      <TopBar title="대출 납입 계산기"/>
      <div className="autotransfer-container pt-5">
        <div className="font-bold mb-2">대출금액</div>
        <div className="flex mb-5">
          <input type="number" placeholder="금액입력" 
          className="input input-bordered w-full" 
          onChange={(e)=>setLoanAmount(e.target.value)}/>
          <div className="flex items-center font-bold text-md ml-2">원</div>
        </div>
        <div className="font-bold mb-2">대출기간</div>
        <div className="flex mb-5">
          <input type="number" placeholder="기간입력" 
          className="input input-bordered w-full" style={{ flex:20 }} 
          onChange={(e)=>setloanPeriod(e.target.value)}/>
          <div className="flex items-center font-bold text-md ml-2" >개월</div>
        </div>
        <div className="font-bold mb-2">대출금리</div>
        <div className="flex mb-5">
          <input type="number" placeholder="금리입력" 
          className="input input-bordered w-full"
          onChange={(e)=>setLoanRate(e.target.value)} />
          <div className="flex items-center font-bold text-md ml-2">%</div>
        </div>
        <div className="font-bold mb-2">상환방법</div>
        <div className="flex mb-5">
          <input type="text" value="원리금 균등 분할 상환"
          className="input input-bordered w-full readonly-input"
          readOnly/>
        </div>
        <button className="btn w-full mb-2 text-lg"
        onClick={calculateLoan}
        style={{ backgroundColor:'rgba(46, 177, 173, 0.8)',color:'white'}}>계산하기</button>
        <div className="flex pt-5">
          <div style={{ flex:5,color:'#827979' }}>총 상환금액</div>
          <div className="font-bold text-xl" style={{ color:'red' }}>{totalPayment}원</div>
        </div>
        <div className="flex pt-2">
          <div style={{ flex:5,color:'#827979' }}>매월내야하는 금액</div>
          <div>{monthlyPayment}원</div>
        </div>
        <div className="flex pt-2">
          <div style={{ flex:5,color:'#827979' }}>총이자</div>
          <div>{totalInterest}원</div>
        </div>
      </div>
    </div>
  )
}