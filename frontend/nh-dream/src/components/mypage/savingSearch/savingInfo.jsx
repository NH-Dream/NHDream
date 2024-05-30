import { useEffect, useState } from "react"
import savingImg from "../../../assets/images/savingIMG.png"
import "../../../assets/css/size.css"
import "../../../assets/css/text.css"
import { useParams } from "react-router-dom"
import { checkSavingDetail } from "@/services/myproduct"

export default function SavingDetailComponent() {
  const param = useParams()
  const id = param.id
  const[info,setInfo] = useState([])

  const formattedBalance = info.balance ? `${info.balance.toLocaleString()}원` : '0원';

  useEffect(()=>{
    checkSavingDetail(
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
      <div className="savingColor flex flex-col pb-3">
        <div className="mt-5 mb-8">
          <div className="largeText pl-5">{info.name}</div>
          <div className="pl-5 mt-3 text-lg">{info.accountNum}</div>
        </div>
        <div className="largeText grid grid-cols-2">
          <img src={savingImg} alt="저금 이미지" className="col-span-1"/>
          <div className="flex flex-col justify-center col-span-1">
            <span className="largeText text-center">{formattedBalance}</span>
          </div>
        </div>
      </div>

    
      <div className="flex justify-between p-3 text-xl font-bold" style={{ borderBottom: '1px solid #ccc'}}>
        <div>적금정보</div>
        { info.type === 1 && (<div className="text-[red]">(만기완료)</div>)}
      </div>
      <div className="p-3">
        <div className="flex justify-between middleText mb-3">
          <span>현재예금액</span>
          <span className="font-bold">{formattedBalance}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>적용금리</span>
          <span className="font-bold">{info.interestRate}%</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>신규가입일</span>
          <span className="font-bold">{info.joinDate}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기일</span>
          <span className="font-bold">{info.expiredAt}</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>월불입액</span>
          <span className="font-bold">{info.monthlyAmount}원</span>
        </div>
        <div className="flex justify-between middleText mb-3">
          <span>만기시 처리방법</span>
          <span className="font-bold">만기시자동이체</span>
        </div>
      </div>
    </div>
  )
}