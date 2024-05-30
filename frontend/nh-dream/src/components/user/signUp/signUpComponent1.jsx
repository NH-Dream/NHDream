import { useCallback, useState } from "react"
import { useNavigate } from "react-router-dom"
import { useStore } from "../../../stores/userStore"
import {checkDuplicateId, sendEmail, verifyCode} from "../../../services/user"
import Swal from "sweetalert2"
import NHdream from "../../../assets/images/NHdreamGuest.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function SignUpComponent1() {
  const navigate = useNavigate()
  
  // 성공 모달
  const successModal = (message) => {
    Swal.fire({
      icon: "success",
      title: `${message}`,
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
  
  // 페이지 이동
  const movePage = () => {
    // 입력 정보 유효성 검사
    if(!idValidation || !emailValidation || !passwordValidation){
      failModal(`입력정보를 다시 한번 \n 확인해주세요.`)
      return;
    }

    // zustand 저장 
    useStore.setState({
      loginId: id,
      email: email,
      password: password
    });
  

    // 페이지 이동
    navigate("/signUp/2")
  }


  // 정규표현식
  const idRegex = /^(?=.*\d)[a-zA-Z0-9_]{5,12}$/;
  const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/i;
  const passwordRegex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;


  // 아이디
  const [id, setId] = useState("")
  const [idValidation, setIdValidation] = useState(false)
  const [checkIdMessage, setCheckIdMessage] = useState('')

  // 아이디 유효성 검사(정규식, 중복)
  const checkId = () => {
    // 정규식 체크
    if(!idRegex.test(id)){
      setIdValidation(false);
      setCheckIdMessage('영문, 숫자 포함한 5~12자')
      return;
    }
    // 중복검사 API 
    checkDuplicateId(
      id,
      response => {
        setIdValidation(true);
      },
      error => {
        console.log('아이디 중복검사 실패', error)
        if (error.response.data.dataHeader.resultCode === "U-002") {
          setIdValidation(false);
          setCheckIdMessage('이미 사용중인 아이디입니다.')
        }
      },
    );
  }


  // 이메일
  const [email, setEmail] = useState("")
  const [emailValidation, setEmailValidation] = useState(false)
  const [checkEmailMessage, setCheckEmailMessage] = useState('')
  const [authCode, setAuthCode] = useState("")

  // 이메일 전송
  const sendCode = () => {
    // 정규식 체크
    if(!emailRegex.test(email)){
      setEmailValidation(false);
      setCheckEmailMessage("형식에 맞지 않는 이메일입니다.")
      return;
    }

    // 이메일 전송 API
    sendEmail(
      {
        email:email, 
        loginId:id,
      },
      response => {
        successModal("인증번호 발송 성공")
      },
      error => {
        console.log('이메일 전송 실패', error)
        if (error.response.data.dataHeader.resultCode === "U-002") {
          setEmailValidation(false);
          failModal('유효하지 않은 이메일입니다.')
        }
      },
    );
  }

  // 인증 코드 인증
  const verifyEmailCode = () => {
    verifyCode(
      {
        email, 
        authCode,
      },
      response => {
        setEmailValidation(true);
        successModal("인증되었습니다.") 
      },
      error => {
        setEmailValidation(false);
        console.log('코드 인증 실패', error)
        let message = ""
        if (error.response.data.dataHeader.resultCode === "U-005") {
          message = "인증 시간이 초과하였습니다."
        }else if(error.response.data.dataHeader.resultCode === "U-006"){
          message = "인증 코드가 일치하지 않습니다."
        }
        failModal(message);
      },
    );
  }


  // 비밀번호
  const [password, setPassword] = useState("")
  const [realPassword, setRealPassword] = useState("")
  const [passwordValidation, setPasswordValidation] = useState(false)
  const [checkPasswordMessage, setCheckPasswordMessage] = useState("")

  // 유효성 검사
  const validatePassword = useCallback(() => {
    if(!passwordRegex.test(password)){
      setPasswordValidation(false)
      setCheckPasswordMessage("숫자, 문자, 특수문자 포함 \n 8~15자리 이내여야 합니다.")
    }
  }, [password])

  // 비밀번호 일치 확인
  const confirmPasswordMatch = useCallback((secret) =>{
    if(!passwordRegex.test(password) || !passwordRegex.test(secret)){
      setPasswordValidation(false)
      setCheckPasswordMessage("숫자, 문자, 특수문자 포함 \n 8~15자리 이내여야 합니다.")
      return
    }

    if(password === secret){
      setPasswordValidation(true)
    }else{ 
      setPasswordValidation(false)
      setCheckPasswordMessage('비밀번호가 서로 일치하지 않습니다.');
    }
  }
  ,[password])

  return (
    <div className="container">
      <div className="ml-5">
        <img
          className="logoImg loginSize"
          src={NHdream}
          alt="NHdream"
          onClick={() => navigate("/login")}
        />
      </div>
      <div className="loginSize mb-5">
        <ul className="steps">
          <li className="step step-neutral"></li>
          <li className="step"></li>
          <li className="step"></li>
          <li className="step"></li>
          <li className="step"></li>
        </ul>
      </div>

      {/* 아이디 */}
      <div
        className="loginSize"
      >
        <input
          value={id}
          onChange={(event) => {setId(event.target.value), setCheckIdMessage('')}}
          onBlur = {(event) => checkId(event.target.value)}
          type="text"
          id="id"
          name="id"
          className={`input input-bordered inputs loginSize ${idValidation ? 'border-green-500' : ''}`}
          placeholder="아이디"
        />
        <div className="text-red-500" >{checkIdMessage}</div>
      </div>
      
      {/* 이메일 */}
      <div
        className="loginSize"
      >
        <input
          value={email}
          onChange={(event) => {setEmail(event.target.value), setCheckEmailMessage('')}}
          type="email"
          id="email"
          name="email"
          className={`input input-bordered inputs loginSize ${emailValidation ? 'border-green-500' : ''}`}
          placeholder="이메일"
        />
        <p
          className="clickText mr-5 mb-2"
          onClick={() => sendCode()}
        >
          인증번호 발송
        </p>
        <div className="text-red-500" >{checkEmailMessage}</div>
      </div>
      
      {/* 인증번호 확인 */}
      <div
        className="loginSize mb-5"
      >
        <input
          value = {authCode}
          onChange={(event) => {setAuthCode(event.target.value)}}
          type="number"
          id="code"
          name="code"
          className={`input input-bordered inputs loginSizeS ${emailValidation ? 'border-green-500' : ''}`}
          placeholder="인증번호"
        />
        <button
          className="userCheckBtn"
          onClick={() => verifyEmailCode()}
        >
          확인
        </button>
      </div>

      {/* 비밀번호*/}
      <div
        className="loginSize"
      >
        <input
          value={password}
          onChange={(event) => {setPassword(event.target.value), setCheckPasswordMessage("")}}
          onBlur={(event) => {validatePassword(event.target.value)}}
          type="password"
          id="password"
          name="password"
          className={`input input-bordered inputs loginSize ${passwordValidation ? 'border-green-500' : ''}`}
          placeholder="비밀번호"
        />
      </div>
      
      {/* 비밀번호 확인*/}
      <div
        className="loginSize"
      >
        <input
          value={realPassword}
          onChange={(event) => {setRealPassword(event.target.value)}}
          onBlur={(event) => {confirmPasswordMatch(event.target.value)}}
          type="password"
          id="passwordCheck"
          name="passwordCheck"
          className={`input input-bordered inputs loginSize mb-5 ${passwordValidation ? 'border-green-500' : ''}`}
          placeholder="비밀번호 확인"
        />
        <div className="text-red-500" style={{ whiteSpace: 'pre-line' }} >{checkPasswordMessage}</div>

        <button
          className="userBtn1 loginSize mt-2"
          onClick={() => movePage()}
        >
          다음
        </button>
      </div>
    </div>
  )
}