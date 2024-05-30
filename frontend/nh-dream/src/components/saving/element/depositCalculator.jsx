import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { modalStore, userStore } from "@/stores/userStore"
import { DepositDetail, DepositList } from "@/services/deposit"
import moreBar from "../../../assets/images/moreBar.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositCalculatorComponent() {
  const store = userStore()
  const modal = modalStore()
  const navigate = useNavigate()

  const [depositList, setDepositList] = useState([])

  const [myDepositId, setMyDepositId] = useState(0)
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

  const chooseDepositId = async (deposit) => {
    await setMyDepositId(deposit)
    DepositDetail(
        deposit,
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
    if (myDepositId) {
      navigate(`/deposit/${myDepositId}`)
    } else {
      modal.failModal("상품을 선택해주세요.")
    }
  }
  
  useEffect(() => {
    DepositList(
      res => {
        setDepositList(res)
      },
      err => {
        console.log("예금 목록 불러오기 실패", err)
      }
    )
  }, [])

  useEffect(() => {
    if (money) {
      setFinalMoney(money*((((myInterest/100)/12)*month)+1))
    }
  }, [myInterest, month, money])

  useEffect(() => {
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
        <span className="middleText whiteColor">예금 만기 금액 계산기</span>
        <img src={moreBar} alt="하단아이콘" className="w-6 h-3 mt-2" />
      </div>
      <div className="collapse-content flex flex-col">
        <div className="whiteColor">
          <select className="selects select-ghost select-accent w-full" value={myDepositId} onChange={(event) => { chooseDepositId(event.target.value) }}>
            <option value={0} disabled>예금을 선택해주세요.</option>
            {depositList?.map(deposit => (
              <option key={deposit.id} value={deposit.id}>{deposit.name}</option>
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
          <span className="whiteColor">원 예치하면?</span>
        </div>
        <div className="mt-2 text-yellow-300 text-end">
          <div>(세전) 만기 시 <span className="largeText ">{makeMoney(Math.floor(finalMoney))}원</span></div>
        </div>
        <div className="text-end whiteColor">
          이자 연 {myInterest}% | 이자총액 {makeMoney(Math.floor(money*(((myInterest/100)/12)*month)))}원
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