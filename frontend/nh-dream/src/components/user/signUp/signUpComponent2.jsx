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

export default function SignUpComponent2() {
  const navigate = useNavigate()

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
    if(!nameValidation || !birthDateValidation || !phoneValidation){
      failModal(`입력정보를 다시 한번 \n 확인해주세요.`)
      return;
    }

    // zustand 저장 
    useStore.setState({
      name: name,
      phone: phone,
      birthDate: birthDate
    });
  
    // 페이지 이동
    navigate("/signUp/3")
  }
  
    // 정규표현식
    const nameRegex = /^[가-힣]{2,}$/;
    const phoneRegex = /^(01[016789]{1})(?:-?[0-9]{3,4}){2}$/

  // 이름
  const [name, setName] = useState("")
  const [nameValidation, setNameValidation] = useState(false)
  const [checkNameMessage, setCheckNameMessage] = useState("")

  const validateName = useCallback(() =>{
    if(!nameRegex.test(name)){
      setNameValidation(false);
      setCheckNameMessage("유효한 한글 이름을 입력해주세요.")
    }else{
      setNameValidation(true);
    }
  }, [name])

  // 생년월일
  const [birthDate, setBirthDate] = useState("")
  const [birthDateValidation, setBirthDateValidation] = useState(false)

  // 전화번호
  const [phone, setPhone] = useState("")
  const [phoneValidation, setPhoneValidation] = useState(false)
  const [checkPhoneMessage, setCheckPhoneMessage] = useState("")
  const validatePhone = useCallback(() =>{
    if(!phoneRegex.test(phone)){
      setPhoneValidation(false);
      setCheckPhoneMessage("유효한 전화번호를 입력해주세요.")
    }else{
      setPhoneValidation(true);
    }
  }, [phone])
  
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
          <li className="step"></li>
          <li className="step"></li>
          <li className="step"></li>
        </ul>
      </div>

      {/* 이름 */}
      <div
        className="loginSize mb-3"
      >
        <input
          value={name}
          onChange={(event) =>{setName(event.target.value), setCheckNameMessage("")}}
          onBlur={(event) => {validateName(event.target.value)}}
          type="text"
          id="name"
          name="name"
          className={`input input-bordered inputs loginSize ${nameValidation ? 'border-green-500' : ''}`}
          placeholder="이름"
        />
        <div className="text-red-500" >{checkNameMessage}</div>
      </div>

      {/* 생년월일 */}
      <div
        className="loginSize mb-3 ml-8"
      >
        <label className={`loginSize input input-bordered flex items-center gap-2 ${birthDateValidation ? 'border-green-500' : ''}`}>
          생년월일
          <input
            onChange={(event) => {setBirthDate(event.target.value), setBirthDateValidation(true)}}
            type="date"
            id="birth"
            name="birth"
            max="9999-12-31"
            placeholder="생년월일"
          />
        </label>
      </div>

      {/* 전화번호 */}
      <div
        className="loginSize mb-5"
      >
        <input
          value={phone}
          onChange={(event) =>{setPhone(event.target.value), setCheckPhoneMessage("")}}
          onBlur={(event) => {validatePhone(event.target.value)}}
          type="text"
          id="phoneNumber"
          name="phoneNumber"
          className={`input input-bordered inputs loginSize ${phoneValidation ? 'border-green-500' : ''}`}
          placeholder="전화번호"
        />
        <div className="text-red-500" >{checkPhoneMessage}</div>
      </div>

      {/* 다음 버튼 */}
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