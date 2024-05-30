import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { SavingDetail, SavingList } from "@/services/saving"
import { modalStore, userStore } from "@/stores/userStore"
import moreBar from "../../../assets/images/moreBar.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function SavingCalculatorComponent() {
  const store = userStore()
  const modal = modalStore()
  const navigate = useNavigate()

  const [savingList, setSavingList] = useState([])

  const [mySavingId, setMySavingId] = useState(0)
  const [money, setMoney] = useState(0)
  const [month, setMonth] = useState(0)
  const [myInterest, setMyInterest] = useState(0)
  const [finalMoney, setFinalMoney] = useState(0)

  const [options, setOptions] = useState({
    graduateRate6Months: 0,
    graduateRate12Months: 0,
    graduateRate24Months: 0,
    farmerRate6Months: 0,
    farmerRate12Months: 0,
    farmerRate24Months: 0,
  });

  const makeMoney = (money) => {
    return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  const chooseSavingId = async (saving) => {
    await setMySavingId(saving)
      SavingDetail(
        saving,
        res => {
          setOptions({
            graduateRate6Months: res.graduateRate6Months,
            graduateRate12Months: res.graduateRate12Months,
            graduateRate24Months: res.graduateRate24Months,
            farmerRate6Months: res.farmerRate6Months,
            farmerRate12Months: res.farmerRate12Months,
            farmerRate24Months: res.farmerRate24Months,
          })
        },
        err => {
          console.log(err, "상세정보 받아오기 실패")
        }
      )
  }

  const movePage = () => {
    if (mySavingId) {
      navigate(`/saving/${mySavingId}`)
    } else {
      modal.failModal("상품을 선택해주세요.")
    }
  }

  useEffect(() => {
    SavingList(
      res => {
        setSavingList(res)
      },
      err => {
        console.log("적금 목록 불러오기 실패", err)
      }
    )
  }, [])

  useEffect(() => {
    const calculateMaturityAmount = () => {
      const interestRatePerMonth = myInterest / 12;
      let totalInterest = 0
      for (let i = 0; i < month; i++) {
        const monthlyInterest = money * interestRatePerMonth * (1+i);
        totalInterest += monthlyInterest
      }
      setFinalMoney(totalInterest/100 + money*month)
    };
    calculateMaturityAmount();
  }, [myInterest, month, money])

  useEffect(() => {
    const myType = (month) => {
      if (store.userType == "TRAINEE") {
        if (month == 6) {
          setMyInterest(Math.floor(options.graduateRate6Months*10000)/100)
        } else if (month == 12) {
          setMyInterest(Math.floor(options.graduateRate12Months*10000)/100)
        } else if (month == 24) {
          setMyInterest(Math.floor(options.graduateRate24Months*10000)/100)
        } else {
          setMyInterest(0)
          console.log("수료자-여기오면 안돼")
        }
      } else if (store.userType == "FARMER") {
        if (month == 6) {
          setMyInterest(Math.floor(options.farmerRate6Months*10000)/100)
        } else if (month == 12) {
          setMyInterest(Math.floor(options.farmerRate12Months*10000)/100)
        } else if (month == 24) {
          setMyInterest(Math.floor(options.farmerRate24Months*10000)/100)
        }
      } else {
        console.log("유저타입이 설정 안되었어요!")
        setMyInterest(888)
      }
    }
    if (month && options) {
      myType(month);
    }
  }, [month, options, store.userType]);

  return (
    <div className="collapse mainColor mt-5">
      <input type="checkbox" />
      <div className="collapse-title pr-3 text-xl font-medium middleText flex justify-between">
        <span className="middleText whiteColor">적금 목표 금액 계산기</span>
        <img src={moreBar} alt="하단아이콘" className="w-6 h-3 mt-2" />
      </div>
      <div className="collapse-content flex flex-col">
        <div className="whiteColor">
          <select className="selects select-ghost select-accent w-full" value={mySavingId} onChange={(event) => { chooseSavingId(event.target.value) }}>
            <option value={0} disabled>적금을 선택해주세요.</option>
            {savingList?.map(saving => (
              <option key={saving.id} value={saving.id}>{saving.name}</option>
            ))}
          </select>
        </div>
        <div className="flex mt-3">
          <div className="whiteColor">
            <select className="selects select-ghost w-full" value={month} onChange={(event) => { setMonth(event.target.value) }}>
              <option value={0} disabled>개월 선택</option>
              <option value={6}>6개월</option>
              <option value={12}>12개월</option>
              <option value={24}>24개월</option>
            </select>
          </div>
          <div className="flex justify-center items-center">
            <span className="whiteColor">동안</span>
          </div>
        </div>
        <div className="mt-3">
          <input type="number" className="w-1/3 ml-2" onChange={(event) => setMoney(event.target.value)} />
          <span className="whiteColor">원 씩 모으면?</span>
        </div>
        <div className="mt-2 text-yellow-300 text-end">
          <div>(세전) 만기 시 <span className="largeText ">{makeMoney(finalMoney)}원</span></div>
        </div>
        <div className="text-end whiteColor">
          이자 연 {myInterest}% | 원금총액 {makeMoney(money*month)}원
        </div>
        <div className="mt-5">
          <button
            className="userBtn2 w-full h-10 flex justify-center items-center"
            onClick={() => {
              movePage()
            }}
          >
            <span>가입하기</span>
          </button>
        </div>
      </div>
    </div>
  )
}