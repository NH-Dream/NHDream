import { useNavigate } from "react-router-dom"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"
import {logout} from "../../../services/user"
import {userStore} from "../../../stores/userStore"
import secureLocalStorage from "react-secure-storage"


export default function JudgingComponent() {
  const navigate = useNavigate()
  const user = userStore();

  const movePage = () => {
    goLogout();
    navigate("/login")
  }

  // 로그아웃
  const goLogout = () => {
    logout(
      response => {
        secureLocalStorage.removeItem("access")
        user.logout()
        navigate("/login");
      },
      error => {
        console.log("로그아웃 실패")
      },
    );
  }

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <div className="flex flex-col items-center justify-center">
        <h2 className="font-semibold largeText">관리자가 승인대기중입니다...</h2>
        <p>승인 완료 후 사용 가능합니다.</p>
      </div>
      <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/People/Farmer.png" alt="Farmer" className="loginSize" />
      <div className="loginSize">
        <button
          className="btn loginSize"
          onClick={() => movePage()}
        >
          로그인으로 가기
        </button>
      </div>
    </div>
  )
}