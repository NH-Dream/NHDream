import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useCallback, useState } from "react"
import { useNavigate } from "react-router-dom"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"
import {applyFarmer} from "../../../services/user"
import {useStore, modalStore} from "../../../stores/userStore"

export default function SignUpComponent5Farmer() {
  const navigate = useNavigate()
  const store = useStore();
  const modal = modalStore(); 

  const movePage = () => {
    if(!licenseNumValidation || !representativeValidation || !registrationDateValidation || !buisinessImgValidation){
      modal.failModal(`입력정보를 다시 한번 \n 확인해주세요.`)
      return
    }

    const formData = new FormData();
    const json = JSON.stringify({
      licenseNum: licenseNum,
      representative: representative,
      registrationDate: registrationDate,
      loginId: store.loginId 
    })
    const blob = new Blob([json], { type: "application/json" });

    formData.append('applyFarmerReqDto', blob);
    formData.append('buisinessImg', buisinessImg);

    applyFarmer(
      formData,
      response => {
        modal.successModal("귀농 인증 신청되었습니다.")
        navigate("/login")
      },
      error => {
        if (error == "U-009") {
          modal.failModal("유효하지 않은<br/>사업자입니다.")
          return
        }
        modal.failModal("귀농 인증 신청이 \n 실패되었습니다.")
      },
    );
  }

  // 정규표현식
  const nameRegex = /^[가-힣]{2,}$/;
  const businessRegex = /^\d{10}$/;


  const [licenseNum, setLicenseNum] = useState(0);
  const [licenseNumValidation, setLicenseNumValidation] = useState(false);
  const [licenseNumMessage, setLicenseNumMessage] = useState("");

  const validateLicenseNum = () => { 
    if(!businessRegex.test(licenseNum)){
      setLicenseNumValidation(false)
      setLicenseNumMessage("- 제외한 10자리 숫자로 작성해주세요.")
      return
    }

    setLicenseNumValidation(true) 
  }

  // 대표명
  const [representative, setRepresentative] = useState(""); 
  const [representativeValidation, setRepresentativeValidation] = useState(false);
  const [representativeMessage, setRepresentativeMessage] = useState("");

  const validateReprsentative = useCallback(() => {
    if(!nameRegex.test(representative)){
      setRepresentativeMessage("유효한 한글 이름을 입력해주세요.")
      setRepresentativeValidation(false)
      return
    }

    if(representative != store.name){
      setRepresentativeMessage("가입하신 이름과 일치하지 않습니다.")
      setRepresentativeValidation(false)
      return
    } 

    setRepresentativeValidation(true)

  }, [representative])

  // 등록일
  const [registrationDate, setRegistrationDate] = useState("");
  const [registrationDateValidation, setRegistrationDateValidation] = useState(false);

  // 이미지
  const [buisinessImg, setBuisinessImg] = useState(null);
  const [buisinessImgValidation, setBuisinessValidation] = useState(false);
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setBuisinessImg(file)
      setBuisinessValidation(true)
    }else{
      setBuisinessValidation(false)
    }
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
          <li className="step step-neutral"></li>
        </ul>
      </div>
      
      <div className="loginSize mb-5">
        {/* 사업자 등록 번호 */}
        <input 
          onChange={(event) => {setLicenseNum(event.target.value), setLicenseNumMessage("")}}
          onBlur={(event) => validateLicenseNum()}
          type="number" 
          className={`input input-bordered loginSize mb-3 ${licenseNumValidation ? 'border-green-500' : ''}`}
          placeholder="사업자 등록번호" 
        />
        <div className="text-red-500" >{licenseNumMessage}</div>
        
        {/* 대표자 성명 */}
        <input
          value={representative}
          onChange={(event) => {setRepresentative(event.target.value), setRepresentativeMessage("")}}
          onBlur={(event) => {validateReprsentative(event.target.value)}}
          type="text"
          id="businessNumber"
          name="businessNumber"
          className={`input input-bordered inputs loginSize mb-3 ${representativeValidation ? 'border-green-500' : ''}`}
          placeholder="대표자 성명"
        />
        <div className="text-red-500" >{representativeMessage}</div>

        {/* 개업일자 */}
        <label className={`loginSize input input-bordered flex items-center gap-2 ml-4 mb-3 ${registrationDateValidation ? 'border-green-500' : ''}`}>
          개업일자
          <input
            onChange={(event) => {setRegistrationDate(event.target.value), setRegistrationDateValidation(true)}}
            type="date"
            id="birth"
            name="birth"
            max="9999-12-31"
            placeholder="생년월일"
          />
        </label>

        {/* 사진 */}
        <div className="loginSize ml-1">
          <input 
            type="file" 
            className={`file-input file-input-bordered loginSize ${buisinessImgValidation ? 'border-green-500' : ''}`}
            onChange={handleFileChange} 
          />
        </div>
      </div>

      {/* 버튼 */}
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => movePage()}
        >
          농부인증
        </button>
      </div>
    </div>
  )
}