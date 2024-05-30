import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { changeWalletPassword } from "@/services/wallet"
import Swal from "sweetalert2"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function ChangeWalletPasswordComponent() {
  const navigate = useNavigate()

  const [prePassword, setPrePassword] = useState("")
  const [newPassword, setNewPassword] = useState("")
  const [newPasswordConfirm, setNewPasswordConfirm] = useState("")

  const [passwordValidation, setPasswordValidation] = useState(false)
  const [checkPasswordMessage, setCheckPasswordMessage] = useState("")

  const validatePassword = (imPassword) => {
      if (imPassword.length != 6) {
        setPasswordValidation(false)
        setCheckPasswordMessage("6자리 숫자로 입력해주세요.")
      } else {
        setPasswordValidation(true)
        setNewPassword(imPassword)
        setCheckPasswordMessage("")
      }
  }

  const handleChangePassword = () => {
    if (prePassword && newPassword && newPassword == newPasswordConfirm && passwordValidation) {
      changeWalletPassword(
        {
          password: prePassword,
          newPassword1: newPassword,
          newPassword2: newPasswordConfirm
        },
        res => {
          Swal.fire({
            icon: "success",
            html: "<b>지갑비밀번호가 변경되었습니다</b>",
            showCloseButton: true,
            showConfirmButton: false
          })
          navigate('/mypage/mywallet')
        },
        err => {
          console.log(err)
          if (err == "F-003") {
            Swal.fire({
              icon: "error",
              html: "<b>기존 지갑 비밀번호가 틀립니다.</b>",
              showCloseButton: true,
              showConfirmButton: false
            })
            navigate('/mypage/mywallet/changePassword')
          } else if (err == "U-012") {
            Swal.fire({
              icon: "error",
              html: "<b>새비밀번호와 확인 값이 일치하지 않습니다.</b>",
              showCloseButton: true,
              showConfirmButton: false
            })
          }
        }
      )
    } else if (newPassword != newPasswordConfirm) {
      Swal.fire({
        icon: "error",
        html: "<b>비밀번호가 일치하지 않습니다</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
      return;
    } else if (!passwordValidation) {
      Swal.fire({
        icon: "error",
        html: "<b>새비밀번호는 6자리로 입력해주세요.</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
    } else {
      Swal.fire({
        icon: "error",
        html: "<b>비밀번호를<br/>확인해주세요.</b>",
        showCloseButton: true,
        showConfirmButton: false
      })
    }
  }


  return (
    <div className="container">
      <div className="loginSize mb-5">
        <h2 className="font-semibold largeText">지갑 비밀번호 변경</h2>
        <p>6자리 숫자를 입력해주세요.</p>
      </div>
      <div className="loginSize mb-2">
        <input
          type="password"
          id="myPassword"
          name="myPassword"
          className="input input-bordered inputs loginSize"
          placeholder="기존비밀번호"
          onChange={(e) => setPrePassword(e.target.value)}
        />
      </div>
      <div className="loginSize mb-2">
        <input
          type="password"
          id="newPassword"
          name="newPassword"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호"
          onChange={(e) => setNewPassword(e.target.value)}
          onBlur={(event) => validatePassword(event.target.value)}
        />
      </div>
      <div className="text-red-500" style={{ whiteSpace: 'pre-line' }} >{checkPasswordMessage}</div>
      <div className="loginSize mb-5">
        <input
          type="password"
          id="newPasswordCheck"
          name="newPasswordCheck"
          className="input input-bordered inputs loginSize"
          placeholder="새비밀번호 확인"
          onChange={(e) => setNewPasswordConfirm(e.target.value)}
        />
      </div>
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => handleChangePassword()}
        >
          비밀번호 변경
        </button>
      </div>
    </div>
  )
}