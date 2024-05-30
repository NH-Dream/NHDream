import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { passwordChange } from "@/services/user"
import { modalStore } from "@/stores/userStore"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function ChangeMyPasswordComponent() {
  const navigate = useNavigate()
  const modal = modalStore()

  const [password, setPassword] = useState("")
  const [newPassword, setNewPassword] = useState("")
  const [newPasswordCheck, setNewPasswordCheck] = useState("")

  const [passwordValidation, setPasswordValidation] = useState(false)
  const passwordRegex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
  const [checkPasswordMessage, setCheckPasswordMessage] = useState("")

  const validatePassword = (imPassword) => {
    if(!passwordRegex.test(imPassword)){
      setPasswordValidation(false)
      setCheckPasswordMessage("숫자, 문자, 특수문자 포함 \n 8~15자리 이내여야 합니다.")
    } else {
      setPasswordValidation(true)
      setNewPassword(imPassword)
      setCheckPasswordMessage("")
    }
  }

  const movePage = () => {
    if (password && newPassword && newPassword == newPasswordCheck && passwordValidation) {
      passwordChange(
        {
          password: password,
          newPassword1: newPassword,
          newPassword2: newPasswordCheck
        },
        res => {
          navigate('/mypage/myInfo')
          modal.successModal("비밀번호가<br/>변경되었습니다.")
        },
        err => {
          console.log("비밀번호 변경 오류", err)
        }
        )
    } else if (newPassword != newPasswordCheck) {
      modal.failModal("설정한 비밀번호 값이<br/>서로 다릅니다.")
    } else {
      modal.failModal("비밀번호를<br/>확인해주세요.")
    }
  }
  return (
    <div className="container">
      <div className="loginSize mb-5">
        <h2 className="font-semibold largeText">비밀번호 변경</h2>
      </div>
      <div className="loginSize mb-2">
        <input
          type="password"
          value={password}
          id="myPassword"
          name="myPassword"
          className="input input-bordered inputs loginSize"
          placeholder="기존비밀번호"
          onChange={(event) => setPassword(event.target.value)}
        />
      </div>
      <div className="loginSize mb-2">
        <input
          type="password"
          value={newPassword}
          id="newPassword"
          name="newPassword"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호"
          onChange={(event) => setNewPassword(event.target.value)}
          onBlur={(event) => validatePassword(event.target.value)}
        />
      </div>
      <div className="text-red-500" style={{ whiteSpace: 'pre-line' }} >{checkPasswordMessage}</div>
      <div className="loginSize mb-5">
        <input
          type="password"
          value={newPasswordCheck}
          id="newPasswordCheck"
          name="newPasswordCheck"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호 확인"
          onChange={(event) => setNewPasswordCheck(event.target.value)}
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