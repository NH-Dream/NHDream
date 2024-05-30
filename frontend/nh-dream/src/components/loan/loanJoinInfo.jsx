import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { LoanJoinInfo } from '@/services/loan'
import "@/assets/css/container.css"
import "@/assets/css/text.css"
import "@/assets/css/button.css"
import "@/assets/css/img.css"
import "@/assets/css/size.css"

export default function LoanJoinInfoComponent({response}) {
  const params = useParams()
  const navigate = useNavigate()

  const [loanData, setLoanData] = useState({})

  const makeMoney = (money) => {
    return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  const movePage = () => {
    navigate(`/loan/${params.id}/joinJudge`, { state : { response } })
  }
  
  useEffect(() => {
    LoanJoinInfo(
      response,
      res => {
        setLoanData(res)
      },
      err => {
        console.error("대출 정보 못받아와요", err)
      }
    )
  }, [])
  return (
      <div className="mx-3">
        <div>
          가입정보를 <span className="middleText">확인</span>해주세요.
          {/* {loanInfoStore.optionId}
          {response} */}
        </div>
        <div className="divider my-2"></div>
        <div className="overflow-x-auto">
          <table className="table">
            <tbody>
              {/* row 1 */}
              <tr className="hover">
                <th>상품명</th>
                <td className="middleText">{loanData.name}</td>
              </tr>
              {/* row 2 */}
              <tr>
                <th>대출기간</th>
                <td className="middleText">{loanData.term}개월</td>
              </tr>
              {/* row 3 */}
              <tr>
                <th>대출금</th>
                <td className="middleText">{makeMoney(Number(loanData.amount))}원</td>
              </tr>
              {/* row 4 */}
              <tr>
                <th>대출이자</th>
                <td className="middleText">{loanData.rate}%</td>
              </tr>
              {/* row 5 */}
              <tr>
                <th>상환방법</th>
                <td className="middleText">원리금 균등상환</td>
              </tr>
              {/* row 6 */}
              <tr>
                <th>상환일</th>
                <td className="middleText">매월 {loanData.paymentDate}일</td>
              </tr>
            </tbody>
          </table>
          <div className="divider my-2"></div>
        </div>
        <div>
          <button 
            className="savingJoinBtn w-full" 
            onClick={() => {
              movePage()
            }}
          >심사신청</button>
        </div>
      </div>
  )
}