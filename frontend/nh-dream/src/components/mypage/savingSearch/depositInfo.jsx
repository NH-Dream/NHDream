import { useEffect, useState } from "react"
import savingImg from "../../../assets/images/savingIMG.png"
import "../../../assets/css/size.css"
import "../../../assets/css/text.css"
import { checkDepositDetail } from "@/services/myproduct"
import { useParams } from "react-router-dom"

export default function DepositDetailComponent() {
  const param = useParams()
  const id = param.id
  const[info,setInfo] = useState([])

  const formattedBalance = info.amount ? `${info.amount.toLocaleString()}원` : '0원'
  const formattedReceivedBalance = info.receivedAmount ? `${info.receivedAmount.toLocaleString()}원` : '0원';

  useEffect(()=>{
    checkDepositDetail(
      id,
      res=>{
        setInfo(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[])



  return (
    <div>
      <div className="depositColor flex flex-col pb-3">
        <div className="mt-5 mb-8">
          <span className="largeText pl-5 ">{info.name}</span>
        </div>
        <div className="largeText grid grid-cols-2">
          <img src={savingImg} alt="저금 이미지" className="col-span-1"/>
          <div className="flex flex-col justify-center col-span-1">
            <span className="largeText text-center">{formattedBalance}</span>
          </div>
        </div>
      </div>
      
      <div className="flex justify-between p-3 text-xl font-bold" style={{ borderBottom: '1px solid #ccc'}}>
        <div>예금정보</div>
        { info.isActive === 1 && (<div className="text-[red]">(만기완료)</div>)}
      </div>
      <div className="p-3">
        <div className="flex justify-between middleText mb-3">
          <span >납입금액</span>
          <span className="font-bold">{formattedBalance}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>적용금리</span>
          <span className="font-bold">{info.appliedInterateRate}%</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기시 예상이자</span>
          <span className="font-bold">{info.expectedInterest}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기시 수령액</span>
          <span className="font-bold">{formattedReceivedBalance}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>신규가입일</span>
          <span className="font-bold">{info.openDate}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기일</span>
          <span className="font-bold">{info.expiredAt}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기시 처리방법</span>
          <span className="font-bold">만기시자동이체</span>
        </div>
      </div>
    </div>
  )
}