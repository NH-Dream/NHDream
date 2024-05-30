import { useNavigate } from "react-router-dom";
import NHdream from "../../../assets/images/NHdreamGuest.png"
import Or from "../../../assets/images/or.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"
import {useState,useEffect} from "react"
import {userStore, modalStore} from "../../../stores/userStore"
import {login} from "../../../services/user"
import { jwtDecode } from 'jwt-decode';
import secureLocalStorage from "react-secure-storage";

export default function LoginComponent() {
  const navigate = useNavigate()
  const modal = modalStore(); 
  const user = userStore();

  const moveToChangPassWord = () => {
    navigate("/password/check")
  }  

  const goLogin  = () => {
    if(id === "" || password === ""){
      modal.failModal("아이디, 비번을 입력해주세요.")
      return; 
    }
    login(
      {
        loginId, 
        password
      },
      response => {
        // accessToken 받기
        
        const accessToken = response.headers.access;
        secureLocalStorage.setItem("access", accessToken)
        const decodeToken = jwtDecode(accessToken)

        user.login();
        user.setUserName(decodeToken.name)
        user.setUserId(decodeToken.userId)
        user.setUserType(decodeToken.userType)
        user.setWalletAddress(decodeToken.walletAddress)
        user.setUserPhone(decodeToken.phone) 

        if(decodeToken.userType === "UNVERIFIED"){
          navigate("/waiting")
          return; 
        }

        navigate("/")
      },
      error => {
        console.log(error);
        modal.failModal("로그인 실패")
      },
    )
  }

  const movePageSignUp = () => {
    navigate("/signUp/1")
  }

  // 아이디
  const [loginId, setLoginId] = useState(""); 

  // 비밀번호
  const [password, setPassword] = useState(""); 

  useEffect(() => {
    const isAccess = secureLocalStorage.getItem('access')
    if (isAccess) {
      secureLocalStorage.removeItem('access')
    }
  }, [])

  return (
    <div className="container">
      {/* 로고 */}
      <div>
        <img
          className="logoImg loginSize"
          src={NHdream}
          alt="NHdream"
        />
      </div>

      {/* 아이디 */}
      <div
        className="mb-2 loginSize"
      >
        <input
          value = {loginId}
          onChange={(event) => setLoginId(event.target.value)}
          type="text"
          id="id"
          name="id"
          className="input input-bordered inputs loginSize"
          placeholder="아이디"
        />
      </div>

      {/* 비밀번호 */}
      <div
        className="loginSize"
      >
        <input
          value = {password}
          onChange={(event) => setPassword(event.target.value)}
          type="password"
          id="password"
          name="password"
          className="input input-bordered inputs loginSize"
          placeholder="비밀번호"
        />
        <p
          className="mb-5 mr-5 clickText"
          onClick={() => moveToChangPassWord()}
        >
          비밀번호 찾기
        </p>
      </div>

      {/* 버튼 */}
      <div className="col">
        <button
          className="userBtn1 loginSize"
          onClick={() => goLogin()}
        >
          로그인
        </button>
        <img
          className="test loginSize"
          src={Or}
          alt="------------or-------------"
        />
        <button
          className="userBtn2 loginSize"
          onClick={() => movePageSignUp()}
        >
          회원가입
        </button>
      </div>
    </div>
  )
}