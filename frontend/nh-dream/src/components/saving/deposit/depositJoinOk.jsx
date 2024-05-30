import { useNavigate } from "react-router-dom"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositJoinOkComponent() {
  const navigate = useNavigate()
  const movePage = () => {
    navigate('/mypage/myaccount')
  }
  return (
    <div className="mx-3 mt-40">
      <div>
        <div className="flex flex-col largeText justify-center items-center">
          <div>
            <span>예금 가입이</span>
          </div>
          <div>
            <span>완료되었습니다.</span>
          </div>
        </div>
        <div className="flex flex-col justify-center items-center mt-5 mb-10">
          <div>
            <span>상세 내용은</span>
          </div>
          <div>
            <span className="middleText">마이페이지 ≫ 마이계좌 ≫ 예·적금</span>에서
          </div>
          <div>
            <span>확인 가능합니다.</span>
          </div>
        </div>
      </div>
      <div
        className="flex justify-center"
        onClick={() => { movePage() }}
      >
        <button className="savingJoinBtn w-11/12">나의 예금 확인하기</button>
      </div>
    </div>
  )
}