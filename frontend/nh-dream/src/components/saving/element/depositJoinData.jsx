import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { DepositDetail } from "@/services/deposit";
import { userStore } from "@/stores/userStore";
import Swal from "sweetalert2";

export default function DepositJoinData() {
  const params = useParams()
  const navigate = useNavigate()
  const store = userStore()

  const [name, setName] = useState("")
  const [errorText, setErrorText] = useState("")
  const [options, setOptions] = useState({})
  const [month, setMonth] = useState(0)
  const [money, setMoney] = useState("")
  const [myInterest, setMyInterest] = useState(0)
  const [depositOption, setDepositOption] = useState(0)

  const [myDeposit, setMyDeposit] = useState({})

  const myType = (month) => {
    if (store.userType == "TRAINEE") {
      if (month == 6) {
        setMyInterest(options.graduateRate6Months)
      } else if (month == 12) {
        setMyInterest(options.graduateRate12Months)
      } else if (month == 24) {
        setMyInterest(options.graduateRate24Months)
      } else {
        setMyInterest(0)
      }
    } else if (store.userType == "FARMER") {
      if (month == 6) {
        setMyInterest(options.farmerRate6Months)
      } else if (month == 12) {
        setMyInterest(options.farmerRate12Months)
      } else if (month == 24) {
        setMyInterest(options.farmerRate24Months)
      } else {
        return setMyInterest(100)
      }
    } else {
      setMyInterest(888)
    }
  }

  const settingMoney = (value) => {
    if (!isNaN(value) && value.trim() !== '') {
      if (value == 0 || options.maximum < value) {
        setErrorText("가입가능금액이 아닙니다.")
      } else {
        setMoney(value)
        setErrorText("")
      }
    } else if (value.trim() === '') {
      setErrorText("공백은 입력할 수 없습니다.")
      setMoney("")
    } else if (isNaN(value)) {
      setErrorText("숫자 외 값은 입력할 수 없습니다.")
    }
  }

  const JoinDeposit = () => {
    if (name && month && money && options && myInterest) {
      if (options.maximum < money) {
        Swal.fire({
          title: "가입한도<br/>초과입니다.",
          icon: "error",
          confirmButtonColor: "#1E7572",
          confirmButtonText: "확인",
        })
      } else {
        setMyDeposit({
          name: name,
          term: month,
          monthlyAmount: money,
          myInterest: myInterest,
          depositOptionId: Number(depositOption)
        })
      }
    } else {
      Swal.fire({
        title: "내용을<br/>입력해주세요.",
        icon: "error",
        confirmButtonColor: "#1E7572",
        confirmButtonText: "확인",
      })
    }
  }

  useEffect(() => {
    DepositDetail(
      params.id,
      res => {
        setOptions({
          maximum: res.maximum,
          option6: res.option6Id,
          option12: res.option12Id,
          option24: res.option24Id,
          graduateRate6Months: res.graduateRate6Months,
          graduateRate12Months: res.graduateRate12Months,
          graduateRate24Months: res.graduateRate24Months,
          farmerRate6Months: res.farmerRate6Months,
          farmerRate12Months: res.farmerRate12Months,
          farmerRate24Months: res.farmerRate24Months,
        })
        setName(res.name)
      },
      err => {
        console.log(err, "상세정보 받아오기 실패", myDeposit)
      }
    )
  }, [])

  useEffect(() => {
    if (month == 6) {
      setDepositOption(options.option6)
      myType(month)
    } else if (month == 12) {
      setDepositOption(options.option12)
      myType(month)
    } else if (month == 24) {
      setDepositOption(options.option24)
      myType(month)
    }
  }, [month])

  useEffect(() => {
    const goJoinInfo = () => {
      if (Object.keys(myDeposit).length !== 0) {
        navigate(`/deposit/${params.id}/join`, { state: { myDeposit } })
      }
    }
    if (myDeposit) {
      goJoinInfo();
    }
  }, [myDeposit])

  return (
    <div>
      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">가입기간</span>
          </div>
          <select className="input input-bordered selects grayText" value={month} onChange={(event) => {
            setMonth(event.target.value)
          }} >
            <option value={0} disabled >가입기간을 선택해주세요.</option>
            <option value={6}>6개월</option>
            <option value={12}>12개월</option>
            <option value={24}>24개월</option>
          </select>
        </label>
      </div>

      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">가입금액</span>
          </div>
          <div className="input input-bordered fake-input">
            <input type="number" 
            placeholder="금액을 입력해주세요." 
             value={money !== null ? money : ''}
             onChange={(event) => settingMoney(event.target.value)} className="w-11/12" />
            <span>원</span>
          </div>
          <div className="label">
            {!money || errorText ? (<span className="label-text-alt text-red-600">{errorText}</span>) :
              (<span className="label-text-alt text-sky-600">가입 가능 금액입니다.</span>)}
          </div>
        </label>
      </div>

      <div>
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">사용자유형</span>
          </div>
          {store.userType == "TRAINEE" && <div className="input input-bordered w-full fake-input" >귀농교육수료자</div>}
          {store.userType == "FARMER" && <div className="input input-bordered w-full fake-input" >귀농인</div>}
        </label>
      </div>

      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">만기해지방법</span>
          </div>
          <div className="input input-bordered w-full fake-input" >자동해지</div>
        </label>
      </div>
      {/* <div className="divider"></div>  */}
      <div className="ml-1">
        <div className="mt-4">
          <div>(세전) 만기 시 {Math.floor(money * (((myInterest / 100) / 12) * month + 1)) ? <span className="largeText">{Math.floor(money * (((myInterest / 100) / 12) * month + 1)).toLocaleString()}</span> : 0}원</div>
        </div>
        <div className="text-gray-600 mb-5">
          이자 연 {myInterest?<span>{myInterest}</span>:0}% | 이자총액 {Math.floor(money * (((myInterest / 100) / 12) * month))? <span>{Math.floor(money * (((myInterest / 100) / 12) * month)).toLocaleString()}</span>:0}원
        </div>
      </div>

      <div className="middleText mt-4 text-red-500">가입한 상품은 재가입이 불가합니다.</div>
      <div>
        <button className="savingJoinBtn w-full mt-5" onClick={() => JoinDeposit()}>가입하기</button>
      </div>
    </div>
  )
}