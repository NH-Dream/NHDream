import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { getUserInfo } from "@/services/user"

export default function MyInfoComponent() {
  const navigate = useNavigate()

  const [name, setName] = useState("")
  const [birth, setBirth] = useState("")
  const [email, setEmail] = useState("")
  const [id, setId] = useState("")
  const [isFarmer, setIsFarmer] = useState("")
  const moveChangePassword = () => {
    navigate('/mypage/myInfo/changePassword')
  }
  const moveBusiness = () => {
    navigate('/mypage/myInfo/beFarmer')
  }
  useEffect(() => {
    getUserInfo(
      res => {
        setName(res.name)
        setBirth(res.birthDay)
        setEmail(res.email)
        setId(res.loginId)
        setIsFarmer(res.type)
      },
      err => {
        console.log("회원정보 받아오기 실패", err)
      }
    )
  }, [])
  return (
    <div className="mt-5 mb-7 px-5 bg-white w-full">
      <div className="flex justify-between">
        <span>이름</span>
        <span className="middleText mb-5">{name}</span>
      </div>
      <div className="flex justify-between">
        <span>생년월일</span>
        <span className="middleText mb-5">{birth}</span>
      </div>
      <div className="flex justify-between">
        <span>이메일</span>
        <span className="middleText">{email}</span>
      </div>
      <div className="divider my-2"></div>
      <div className="flex justify-between">
        <span>아이디</span>
        <span className="middleText mb-5">{id}</span>
      </div>
      <div className="flex justify-between">
        <span>비밀번호</span>
        <button
          onClick={() => moveChangePassword()}
          className="customBtn"
        >
          비밀번호 변경
        </button>
      </div>
      <div className="divider my-2"></div>
      <div className="flex justify-between">
        <span>귀농여부</span>
        {isFarmer == "FARMER" ? (
          <div>
            <span className="middleText mb-3">귀농인</span>
          </div>
        ) : (
          <div>
            <div className="flex justify-end">
              <div>
                <span className="middleText mb-3">귀농예정</span>
              </div>
              <button
                onClick={() => moveBusiness()}
                className="ml-2 customBtn"
              >
                귀농인증
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}