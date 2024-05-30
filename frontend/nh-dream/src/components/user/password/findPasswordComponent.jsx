import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useNavigate } from "react-router-dom"
import Swal from "sweetalert2"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function FindPasswordComponent() {
  const openModal = () => {
    Swal.fire({
      icon: "success",
      title: "인증되었습니다.",
      confirmButtonColor: "#559791",
      confirmButtonText: "확인"
    })
  }
  const navigate = useNavigate()
  const sendCode = () => {
    Swal.fire({
      title: "인증번호가 발송되었습니다.",
      confirmButtonColor: "#559791",
      confirmButtonText: "확인"
    })
  }
  const movePage = () => {
    navigate("/password/change")
  }
  return (
    <div className="container">
      <div>
        <img
          className="logoImg loginSize"
          src={NHdream}
          alt="NHdream"
          onClick={() => navigate("/login")}
        />
      </div>
      <div className="loginSize mb-5">
        <h2 className="font-semibold largeText">비밀번호 찾기</h2>
        <p>본인 인증</p>
      </div>
      <div
        className="loginSize"
      >
        <input
          type="text"
          id="id"
          name="id"
          className="input input-bordered inputs loginSize"
          placeholder="아이디"
        />
      </div>
      <div
        className="loginSize"
      >
        <input
          type="email"
          id="email"
          name="email"
          className="input input-bordered inputs loginSize"
          placeholder="이메일"
        />
        <p
          className="clickText mr-5 mb-2"
          onClick={() => sendCode()}
        >
          인증번호 발송
        </p>
      </div>
      <div
        className="loginSize mb-5"
      >
        <input
          type="number"
          id="code"
          name="code"
          className="input input-bordered inputs loginSizeS"
          placeholder="인증번호"
        />
        <button
          className="userCheckBtn"
          onClick={() => openModal()}
        >
          확인
        </button>
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