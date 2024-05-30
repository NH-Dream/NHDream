import NHdream from "../../../assets/images/NHdreamGuest.png"
import { useCallback, useState } from "react"
import { useNavigate } from "react-router-dom"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"
import {applyEducation} from "../../../services/user"
import {useStore, modalStore} from "../../../stores/userStore"

export default function SignUpComponent5Student() {
  const navigate = useNavigate()
  const store = useStore();
  const modal = modalStore();

  const movePage = () => {
    if(!graduationNoValidation || !educatedImgValidation){
      modal.failModal(`입력정보를 다시 한번 \n 확인해주세요.`)
      return
    }
    const formData = new FormData();
    const json = JSON.stringify({
      ordinal: graduationNo, 
      loginId: store.loginId 
    })
    const blob = new Blob([json], { type: "application/json" });

    formData.append('applyEducationReqDto', blob);
    formData.append('educationImg', educatedImg);

    applyEducation(
      formData,
      response => {
        modal.successModal("교육 인증 신청되었습니다.")
        navigate("/login")
      },
      error => {
        modal.failModal("교육 인증 신청이 \n 실패되었습니다.")
      },
    );
  }

  // 기수
  const [graduationNo, setGraduationNo] = useState(0); 
  const [graduationNoValidation, setGraduationNoValidation] = useState(false);
  const [graduationNoMessage, setGraduationNoMessage] = useState("");
  const checkGraducationNo = useCallback(() => {
    if(graduationNo <= 0){
      setGraduationNoMessage("양수를 입력하세요.")
      setGraduationNoValidation(false) 
      return; 
    }
    setGraduationNoValidation(true) 
  }
  ,[graduationNo])

  // 이미지
  const [educatedImg, setEducatedImg] = useState(null); 
  const [educatedImgValidation, setEducatedImgValidation] = useState(false);
  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setEducatedImg(file)
      setEducatedImgValidation(true)
    }else{
      setEducatedImgValidation(false)
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
      <div>
      <div className="mb-5 mt-2">
        <h2 className="font-semibold largeText">청년농부사관학교 수료인증</h2>
        <p>(졸업기수 입력 및 수료증 첨부)</p>
      </div>

      {/* 기수 */}
      <div className="loginSize ml-3">
        <label className={`input input-bordered flex items-center gap-2 ${graduationNoValidation ? 'border-green-500' : ''}`}>
          <input
            onChange={(event) => {setGraduationNo(event.target.value), setGraduationNoMessage("")}}
            onBlur={(event) => checkGraducationNo()}
            type="number" 
            className="grow" 
            placeholder="기수를 입력하세요"
          />
          기
        </label>
      <div className="text-red-500 mb-3" >{graduationNoMessage}</div>

      {/* 사진 */}
      </div>
        <input 
          type="file" 
          className={`file-input file-input-bordered mb-5 loginSize ${educatedImgValidation ? 'border-green-500' : ''}`}
          onChange={handleFileChange}
        />
      </div>

      {/* 버튼 */}
      <div className="loginSize">
        <button
          className="userBtn1 loginSize"
          onClick={() => movePage()}
        >
          수강생 인증
        </button>
      </div>
    </div>
  )
}