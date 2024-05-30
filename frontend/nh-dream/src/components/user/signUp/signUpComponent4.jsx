import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useState } from "react"
import { useNavigate } from "react-router-dom"
import Swal from "sweetalert2"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function SignUpComponent4() {
  const navigate = useNavigate()
  const movePage = () => {
    if (!checkWho) {
      Swal.fire({
        title : '현재 상태를 선택해주세요.',
        icon : "warning",
        confirmButtonColor: "#559791",
        confirmButtonText: "확인"
      })
    } else {
    if (checkWho == "귀농인") {
      navigate("/signUp/5/Farmer")
    } else {
      navigate("/signUp/5/Student")
    }
  }}
  const [checkWho, setCheckWho] = useState("")
  const checkValue = (value) => {
    setCheckWho(value)
  }
  return (
    <div className="container">
      <div>
        <img
          className="logoImg loginSize"
          src={NHdream}
          alt="NHdream"
        />
      </div>
      <div className="loginSize mb-5">
        <ul className="steps">
          <li className="step step-neutral"></li>
          <li className="step step-neutral"></li>
          <li className="step step-neutral"></li>
          <li className="step step-neutral"></li>
          <li className="step"></li>
        </ul>
      </div>
      <h2 className="loginSize largeText mb-5">귀농상태를 선택해주세요.</h2>
      <div className="flex mb-5">
        <div className="mr-4">
          <input
            className="btn btn-outline w-full px-8 py-14 pt-6 font-bold text-lg checked:backgroundColor"
            type="radio"
            name="options"
            aria-label="귀농예정"
            value="귀농예정"
            onClick={(event) => checkValue(event.target.value)}
            style={{
              backgroundColor: checkWho === "귀농예정" ? '#42C3A8' : '',
              borderColor: '#000000',
              fontWeight: '800',
              color: '#000000',
            }}
          />
        </div>
        <div className="ml-4">
          <input
            className="btn btn-outline w-full px-8 pb-14 pt-6 font-bold text-lg"
            type="radio"
            name="options"
            aria-label="귀농인"
            value="귀농인"
            style={{
              backgroundColor: checkWho === "귀농인" ? '#42C3A8' : '',
              borderColor: '#000000',
              fontWeight: '800',
              color: '#000000',
            }}
            onClick={(event) => checkValue(event.target.value)}
          />
        </div>
      </div>
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => movePage()}
        >
          다음
        </button>
      </div>
    </div>
  )
}