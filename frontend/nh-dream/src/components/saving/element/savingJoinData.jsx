import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { SavingDetail } from "../../../services/saving"
import { userStore } from "@/stores/userStore";
import Swal from "sweetalert2";

export default function SavingJoinData() {
  const params = useParams()
  const navigate = useNavigate()
  const store = userStore()

  const [name, setName] = useState("")
  const [errorText, setErrorText] = useState("")
  const [isOpenDate, setIsOpenDate] = useState(false)
  const [options, setOptions] = useState({})
  const [month, setMonth] = useState(0)
  const [selectedDate, setSelectedDate] = useState(0)
  const [money, setMoney] = useState("")
  const [myInterest, setMyInterest] = useState(0)
  const [savingOption, setSavingOption] = useState(0)

  const [savingInfo, setSavingInfo] = useState({})

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
      if (value == 0 || options.maxMonthlyLimit < value) {
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

  const JoinSaving = () => {
    if (name && month && selectedDate && money && options && myInterest) {
      if (options.maxMonthlyLimit < money) {
        Swal.fire({
          title: "가입한도<br/>초과입니다.",
          icon: "error",
          confirmButtonColor: "#1E7572",
          confirmButtonText: "확인",
        })
      } else {
        setSavingInfo({
          name: name,
          term: month,
          monthlyAmount: money,
          depositDate: selectedDate,
          myInterest: myInterest,
          savingOptionId: Number(savingOption)
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

  const dates = [1, 7, 13, 19, 25, 2, 8, 14, 20, 26,
    3, 9, 15, 21, 27, 4, 10, 16, 22, 28,
    5, 11, 17, 23, 29, 6, 12, 18, 24, 30]
  const dropdownDate = () => setIsOpenDate(!isOpenDate);
  const handleDateClick = (item) => {
    setSelectedDate(item);
    setIsOpenDate(false)
  };

  const calculateMaturityAmount = () => {
    const interestRatePerMonth = myInterest*100 / 12;
    let totalInterest = 0
    for (let i = 0; i < month; i++) {
      const monthlyInterest = money * interestRatePerMonth * (1+i);
      totalInterest += monthlyInterest
    }
    return totalInterest/100 + money*month
  };

  useEffect(() => {
    SavingDetail(
      params.id,
      res => {
        setOptions({
          maxMonthlyLimit: res.maxMonthlyLimit,
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
        console.log(err, "상세정보 받아오기 실패", err)
      }
    )
  }, [])

  useEffect(() => {
    if (month === 6) {
      setSavingOption(options.option6)
      myType(month)
    } else if (month === 12) {
      setSavingOption(options.option12)
      myType(month)
    } else if (month === 24) {
      setSavingOption(options.option24)
      myType(month)
    }
  }, [month])

  useEffect(() => {
    const goJoinInfo = () => {
      if (Object.keys(savingInfo).length !== 0) {
        navigate(`/saving/${params.id}/join`, { state: { savingInfo } })
      }
    }

    if (savingInfo) {
      goJoinInfo();
    }
  }, [savingInfo])

  return (
    <div>

      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">가입기간</span>
          </div>
          <select className="input input-bordered selects grayText" value={month} onChange={(event) => {
            setMonth(Number(event.target.value))
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
            <span className="label-text">매월 납입일</span>
          </div>
          <div className="relative line mb-2 input input-bordered">
            <div
              type="text"
              className="input w-full h-8 fake-input flex justify-start"
              readOnly
              onClick={dropdownDate}
              style={{ color: 'gray' }}
            >
              {selectedDate ? `${selectedDate}일` : "이체날짜를 선택해주세요"}
            </div>
            {isOpenDate && (
              <ul className="dropdown-content2 menu p-2 bg-base-100 w-full absolute mt-1">
                {dates.map((date, index) => (
                  <li key={index} onClick={() => handleDateClick(date)}>
                    <a>{date}</a>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </label>
      </div>

      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">월별 납입액</span>
          </div>
          <div className="input input-bordered fake-input">
            <input type="number" placeholder="금액을 입력해주세요."
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
        <label className="w-full">
          <div className="label">
            <span className="label-text">사용자유형</span>
          </div>
          {store.userType == "TRAINEE" && <div className="input input-bordered w-full fake-input" >귀농교육수료자</div>}
          {store.userType == "FARMER" &&
            <div className="input input-bordered w-full fake-input" >귀농인</div>}
        </label>
      </div>

      <div className="mt-3">
        <label className="w-full">
          <div className="label">
            <span className="label-text">만기해지방법</span>
          </div>
          <div className="input input-bordered w-full fake-input" >자동해지</div>
        </label>
      </div>

<div className="ml-1">
<div className="mt-4">
          <div>(세전) 만기 시 {Math.floor(calculateMaturityAmount()) ? <span className="largeText">{Math.floor(calculateMaturityAmount()).toLocaleString()}</span>:0}원</div>
        </div>
        <div className="text-gray-600 mb-5">
          이자 연 {Math.floor(myInterest*10000)/100 ? <span>{Math.floor(myInterest*10000)/100}</span> : 0} % | 원금총액 {(money*month) ? <span>{(money*month).toLocaleString()}</span>:0}원
        </div>
</div>

      <div className="middleText mt-4 text-red-500">가입한 상품은 재가입이 불가합니다.</div>
      <div>
        <button className="savingJoinBtn w-full mt-5" onClick={() => JoinSaving()}>가입하기</button>
      </div>
    </div>
  )
}