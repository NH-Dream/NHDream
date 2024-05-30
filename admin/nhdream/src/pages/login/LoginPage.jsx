import { useNavigate } from "react-router-dom";
import LoginMain from "@/assets/images/login/loginMain3.png"
import NHdreamLogo from "@/assets/images/nh_dream_dashBoard/NHdreamGuest.png"
import "@/assets/css/test.css"
import Swal from "sweetalert2";

const LoginPage = () => {
    const navigate = useNavigate()
    const movePage = () => {
        // Swal.fire("로그인되었습니다.")
        navigate('/login/second')
    }
    return (
        <div className="flex h-screen">
            <div className="flex justify-center items-center w-1/2 max-h-full bgLogin">
                <div className="w-2/3 object-cover max-h-full">
                    <img src={LoginMain} alt="로그인 화면" />
                </div>
            </div>
            <div className="w-1/2 max-h-full flex justify-center items-center bgLogin2 mb-20">
                <div className="flex flex-col items-center max-h-full">
                    <img src={NHdreamLogo} alt="NH드림 로고" className="w-1/2" />
                    <div className="form-control w-2/3">
                        <div className="label">
                            <span className="label-text">Admin ID</span>
                        </div>
                        <input type="text" className="input input-bordered w-full" placeholder="아이디" />
                    </div>
                    <div className="form-control w-2/3 mt-3">
                        <div className="label">
                            <span className="label-text">Password</span>
                        </div>
                        <input type="password" className="input input-bordered w-full" placeholder="비밀번호" />
                    </div>
                    <div className="mt-7 w-2/3">
                        <button className="btn w-full bg-slate-700 text-white" onClick={() => movePage()}>로그인</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;