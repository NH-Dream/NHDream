import { useState } from "react";
import PasswordInput from "./PasswordInput";
import LoginMain from "@/assets/images/login/loginMain3.png"
import NHdreamLogo from "@/assets/images/nh_dream_dashBoard/NHdreamGuest.png"
import "@/assets/css/test.css"

const SecondaryLoginPage = () => {
  // const [isClick, setIsClick] = useState(0)
  // const openPasswordInput = () => {
  //   setIsClick(!isClick)
  // }
  return (
    <div className="flex h-screen">
      <div className="flex justify-center items-center w-1/2 max-h-full bgLogin">
        <div className="w-2/3 object-cover max-h-full">
          <img src={LoginMain} alt="로그인 화면" />
        </div>
      </div>
      <div className="w-1/2 h-full flex justify-center items-center bgLogin2 mb-20">
        <div className="flex flex-col items-center max-h-full">
          <img src={NHdreamLogo} alt="NH드림 로고" className="w-1/2" />
            <PasswordInput/>
          {/* <div className="mt-7 w-2/3">
            {!isClick && <button className="btn w-full bg-slate-700 text-white" onClick={openPasswordInput}>2차 비밀번호 입력</button>}
          </div> */}
        </div>
      </div>
    </div>
  );
};

export default SecondaryLoginPage;