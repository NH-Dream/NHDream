import { useEffect, useState } from "react"
import { checkMyloanDetail } from "@/services/myproduct"
import { useParams } from "react-router-dom"

export default function LoanCard() {
  const param = useParams()
  const id = param.id
  const [info,setInfo] = useState([])

  useEffect(()=>{
    checkMyloanDetail(
      id,
      res=>{
        setInfo(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[])

  const calculatePercentage = () => {
    const { paidPrincipal, principal } = info;
    if (!paidPrincipal || !principal || principal === 0) {
      return 0; // 유효하지 않은 값 처리
    }
    return (paidPrincipal / principal * 100).toFixed(2);
  }


  const formattedPrincipal = info.paidPrincipal ? `${info.paidPrincipal.toLocaleString()}원` : '0원'


  return (
    <div className="card shadow-xl mt-3 mx-3 bg-white">
      <div className="card-body">
        <h2 className="card-title mb-3">{info.name}</h2>
        <div className="flex justify-between largeText">
          <span className="middleText font-thin">납부한 원금</span>
          <span>{formattedPrincipal}</span>
        </div>
        <div className="mt-3 w-full">
        <progress className="progress progress-success w-full" value={calculatePercentage()} max="100"></progress>
        <p>현재 <span className="largeText text-success font-bold">{calculatePercentage()}%</span> 상환했습니다.</p>
        </div>
      </div>
    </div>
  )
}