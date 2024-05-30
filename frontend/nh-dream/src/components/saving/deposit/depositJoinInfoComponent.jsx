import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { DepositJoin } from "@/services/deposit"
import Swal from "sweetalert2"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositJoinInfoComponent({ myDeposit }) {
  const params = useParams()
  const navigate = useNavigate()

  const [myDepositInfo, setMyDepositInfo] = useState(myDeposit)
  const [imDeposit, setImDeposit] = useState({})

  const makeMoney = (money) => {
    if (typeof money === 'number' && !isNaN(money)) {
      return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
  }

  const passwordInit = () => {
    setImDeposit({
      term: Number(myDepositInfo.term),
      reDepositAmount: Number(myDepositInfo.monthlyAmount),
      depositOptionId: Number(myDepositInfo.depositOptionId),
    })
  }

  const checkDepositInfo = async () => {
    if (imDeposit.depositOptionId) {
      const { value: password } = await Swal.fire({
        title: "지갑 비밀번호 입력",
        input: "password",
        inputPlaceholder: "지갑 비밀번호(6자리) 입력",
        inputAttributes: {
          maxlength: "6",
          autocapitalize: "off",
          autocorrect: "off"
        },
        confirmButtonColor: "#1E7572",
        confirmButtonText: "확인",
      });
      if (password) {
        imDeposit.password = password
        if (imDeposit.password) {
          DepositJoin(
            params.id,
            imDeposit,
            res => {
              navigate(`/deposit/${params.id}/join/done`)
            },
            err => {
              console.log(err, imDeposit)
              console.log("예금 가입 실패")
              if (err.resultCode == "S-004") {
                Swal.fire({
                  title: "이미 가입한<br/>상품입니다.",
                  html: "<a href='/mypage/myaccount' style='text-decoration: underline; font-style: italic;'>나의 계좌 확인</a>",
                  icon: "error",
                  confirmButtonColor: "#1E7572",
                  confirmButtonText: "확인",
                })
              } else if (err.resultCode == "R-006") {
                Swal.fire({
                  title: "지갑 잔액이 부족합니다.",
                  html: "<a href='/mypage/mywallet' style='text-decoration: underline; font-style: italic;'>나의 지갑 잔액 확인</a>",
                  icon: "error",
                  confirmButtonColor: "#1E7572",
                  confirmButtonText: "확인",
                })
              } else if (err.resultCode == "T-003") {
                console.log("토큰 전송 실패")
                Swal.fire({
                  title: "가입 오류",
                  html: "해당화면 캡쳐 후 <br/>관리자에게 전송해주세요.",
                  icon: "error",
                  confirmButtonColor: "#1E7572",
                  confirmButtonText: "확인",
                })
              } else {
                Swal.fire({
                  title: "비밀번호를<br/>확인해주세요.",
                  icon: "error",
                  confirmButtonColor: "#1E7572",
                  confirmButtonText: "확인",
                })
              }
            }
          )
        }
      } else {
        Swal.fire({
          title: "비밀번호를<br/>입력해주세요.",
          icon: "error",
          confirmButtonColor: "#1E7572",
          confirmButtonText: "확인",
        })
      }
    }
  }

  useEffect(() => {
    setMyDepositInfo(myDeposit)
  }, [])

  useEffect(() => {
    if (imDeposit) {
      checkDepositInfo()
    }
  }, [imDeposit])

  return (
    <div className="mx-3">
      <div>
        가입정보를 <span className="middleText">확인</span>해주세요.
      </div>
      <div className="divider my-2"></div>
      <div className="overflow-x-auto">
        <table className="table">
          <tbody>
            {/* row 1 */}
            <tr className="hover">
              <th>상품명</th>
              <td className="middleText">{myDepositInfo.name}</td>
            </tr>
            {/* row 2 */}
            <tr>
              <th>가입기간</th>
              <td className="middleText">{myDepositInfo.term}개월</td>
            </tr>
            {/* row 3 */}
            <tr>
              <th>가입금액</th>
              <td className="middleText">{makeMoney(Number(myDepositInfo.monthlyAmount))}원</td>
            </tr>
            {/* row 4 */}
            <tr>
              <th>적용금리</th>
              <td className="middleText">{myDepositInfo.myInterest}%</td>
            </tr>
            {/* row 5 */}
            <tr>
              <th>적립방식</th>
              <td className="middleText">정기적립</td>
            </tr>
          </tbody>
        </table>
        <div className="divider my-2"></div>
      </div>
      <div className="middleText mb-5 ml-2">가입한 상품은 재가입이 불가합니다.</div>
      <div>
        <button
          className="savingJoinBtn w-full"
          onClick={() => {
            passwordInit()
          }}
        >가입하기</button>
      </div>
    </div>
  )
}