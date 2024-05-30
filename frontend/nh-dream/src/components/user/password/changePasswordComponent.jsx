import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useNavigate } from "react-router-dom"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function ChangePasswordComponent() {
  const navigate = useNavigate()
  const movePage = () => {
    navigate("/login")
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
        <h2 className="font-semibold largeText">비밀번호 재설정</h2>
      </div>
      <div className="loginSize mb-2">
        <input
          type="password"
          id="newPassword"
          name="newPassword"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호"
        />
      </div>
      <div className="loginSize mb-5">
        <input
          type="password"
          id="newPasswordCheck"
          name="newPasswordCheck"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호 확인"
        />
      </div>
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => movePage()}
        >
          비밀번호 변경
        </button>
      </div>
    </div>
  )
}