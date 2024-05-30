import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useNavigate } from "react-router-dom"
import Swal from "sweetalert2"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"
import { useCallback, useState } from "react"
import {useStore} from "../../../stores/userStore"
import {signUp} from "../../../services/user"

export default function SignUpComponent3() {
  const navigate = useNavigate()
  const store = useStore();

    // 성공 모달
    const successModal = () => {
      Swal.fire({
        icon: "success",
        title: "회원가입되었습니다.",
        confirmButtonColor: "#559791",
        confirmButtonText: "확인"
      })
    }

  // 실패 모달
  const failModal = (message) => {
    Swal.fire({
      icon: "error",
      title: `${message}`,
      confirmButtonColor: "#559791",
      confirmButtonText: "확인"
    })
  }

  const movePage = () => {
    // 입력 정보 유효성 검사
    if(!walletPasswordValidation){
      failModal(`입력정보를 다시 한번 \n 확인해주세요.`)
      return;
    }

    // 회원가입
    signUp(
      {
        loginId :store.loginId,
        email :store.email,
        password :store.password,
        name :store.name,
        phone :store.phone,
        birthDate :store.birthDate,
        walletPassword :store.walletPassword,
      },
      response => {
        // successModal();
        navigate("/signUp/4");
      },
      error => {
        failModal("회원가입 실패")
        console.log('회원가입 실패', error)
      },
    );
  }

  // 정규식
  const walletPasswordRegex = /^\d{6}$/

  // 지갑 비밀번호
  const [walletPassword, setWalletPassword] = useState("")
  const [walletPasswordCheck, setWalletPasswordCheck] = useState("")
  const [walletPasswordValidation, setWalletPasswordValidation] = useState(false)
  const [walletPasswordMessage, setWalletPasswordMessage] = useState("")

  // 유효성 검사
  const validateWalletPassword = useCallback(() => {
    if(!walletPasswordRegex.test(walletPassword)){
      setWalletPasswordValidation(false)
      setWalletPasswordMessage("숫자 6자리로 입력해야합니다.")
    }
  }, [walletPassword])

  // 비밀번호 일치 확인
  const confirmWalletPasswordMatch = useCallback((secret) =>{
    if(!walletPasswordRegex.test(walletPassword) || !walletPasswordRegex.test(secret)){
      setWalletPasswordValidation(false)
      setWalletPasswordMessage("숫자 6자리로 입력해야합니다.")
      return;
    }

    if(walletPassword === secret){
      setWalletPasswordValidation(true)
      setWalletPasswordMessage("");
      store.setWalletPassword(walletPassword)
    }else{ 
      setWalletPasswordValidation(false)
      setWalletPasswordMessage('비밀번호가 서로 일치하지 않습니다.');
    }
  }
  ,[walletPassword])

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
          <li className="step"></li>
          <li className="step"></li>
        </ul>
      </div>
      <div className="loginSize mb-5 mt-2">
        <h2 className="font-semibold largeText">지갑 비밀번호 생성</h2>
        <p>6자리를 입력해주세요.</p>
      </div>

      {/* 2차 비밀번호 */}
      <div
        className="loginSize"
      >
        <input
        value={walletPassword}
        onChange={(event) => {setWalletPassword(event.target.value), setWalletPasswordMessage("")}}
        onBlur={(event) => {validateWalletPassword(event.target.value)}}
          type="password"
          id="walletPassword"
          name="walletPassword"
          className="input input-bordered inputs loginSize"
          placeholder="비밀번호 6자리"
        />
      </div>

      {/* 2차 비밀번호 확인 */}
      <div
        className="loginSize mb-5"
      >
        <input
          value={walletPasswordCheck}
          onChange={(event) => {setWalletPasswordCheck(event.target.value), setWalletPasswordMessage("")}}
          onBlur={(event) => {confirmWalletPasswordMatch(event.target.value)}}
          type="password"
          id="walletPasswordCheck"
          name="walletPasswordCheck"
          className="input input-bordered inputs loginSize"
          placeholder="비밀번호 확인"
        />
        <div className="text-red-500" >{walletPasswordMessage}</div>
      </div>

      {/* 버튼 */}
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => movePage()}
        >
          회원가입
        </button>
      </div>
    </div>
  )
}