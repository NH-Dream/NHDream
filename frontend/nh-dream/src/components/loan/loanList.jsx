import { useEffect, useState } from "react"
import { useNavigate } from "react-router"
import { LoanList } from '@/services/loan'
import "../../assets/css/text.css"

export default function LoanListComponent() {
  const navigate = useNavigate()
  const [loanItems, setLoanItems] = useState([])
  const makeMoney = (money) => {
    return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }
  const movePage = (id) => {
    navigate(`/loan/${id}`)
  }
  useEffect(() => {
    LoanList(
      res => {
        setLoanItems(res)
        console.log("대출 목록 불러오기 성공!")
      },
      err => {
        console.log("error", err)
      }
    )
  }, [])

  return (
    <div>
      {loanItems?.length > 0 && loanItems.map((item, index) => (
        <div key={index} className="my-8">
          <div className="flex justify-between mt-3">
            <div className="font-bold text-xl mb-1">{item.name}</div>
            {/* <img src={Next} alt="" style={{ height: 20 }} /> */}
          </div>
          <div className="flex justify-between my-3">
            <div className="flex gap-2">
              <div className="font-bold">최대 한도</div>
              <div className="font-bold middleText text-[#2EB1AD]">{makeMoney(item.amountRange)}원</div>
            </div>
            <div className="flex gap-2">
              <div className="font-bold">금리</div>
              <div className="font-bold text-lg text-[#2EB1AD]">연 {item.minRate} ~ {item.maxRate}%</div>
            </div>
          </div>
          <div className="flex justify-end mt-1">
            <button className="btn w-full bg-[rgb(46,177,173,0.5)] text-lg" onClick={() => movePage(item.id)}>신청하기</button>
          </div>
          <div style={{ borderBottom: '1px solid #ccc' }} className="mt-2"></div>
        </div>
      ))}
    </div>
  )
}