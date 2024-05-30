import { useEffect, useState } from "react"
import "../../../../assets/css/size.css"
import "../../../../assets/css/text.css"
import { checkMyloanDetail } from "@/services/myproduct"
import { useParams } from "react-router-dom"

export default function LoanDetail() {
  const param = useParams()
  const id = param.id
  const [info,setInfo] = useState({})

  useEffect(() => {
    checkMyloanDetail(
        id,
        res=>{
          setInfo(res)
        },
        err=>{
          console.log(err)
        }
      )
  }, [])

  // 원금
  const formattedPrincipal = info.principal ? `${info.principal.toLocaleString()}원` : ''
  // 이자및 수수료
  const formattedInterst = info.interest ? `${info.interest.toLocaleString()}원` : ''
  // 상환후 잔액
  const formattedOutstanding = info.outstandingAmount ? `${info.outstandingAmount.toLocaleString()}원` : ''
  return (
    <div className="my-7  bg-white w-full">
      <div className="p-3 text-xl font-bold" style={{ borderBottom: '1px solid #ccc'}}>
        대출정보
      </div>
      <div className="p-3">
        <div className="flex justify-between middleText mb-3">
          <span>원금</span>
          <span className="font-bold">{formattedPrincipal}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>이자 및 수수료</span>
          <span className="font-bold">{formattedInterst}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>상환 후 잔액</span>
          <span className="font-bold">{formattedOutstanding}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <div className="flex justify-center items-center">
            <span>대출기간</span>
          </div>
          <div>
            <div className="flex justify-end">
              <span className="font-bold">{info.term}개월</span>
            </div>
            <p className="text-sm">({info.startedAt} ~ {info.expirationAt})</p>
          </div>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>납입회차</span>
          <span className="font-bold">{info.round}회차</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>결제방법</span>
          <span className="font-bold">자동이체</span>
        </div>
        <div className="flex justify-between middleText">
          <span>결제일</span>
          <span className="font-bold">매월 {info.paymentDate}일</span>
        </div>
      </div>
    </div>
  )
}