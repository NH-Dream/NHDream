import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { walletPasswordCheck } from "@/services/user";
import { LoanJoin1, LoanDetail } from '@/services/loan'
import { userStore } from "@/stores/userStore";
import { loanStore } from "@/stores/loanStore";
import Swal from "sweetalert2";

export default function LoanJoinData() {
  const params = useParams()
  const store = userStore()
  const loanInfoStore = loanStore()
  const navigate = useNavigate()

  const [financialProducts, setFinancialProducts] = useState([])
  const [finalInterest, setFinalInterest] = useState(0)

  const [isOpenDate, setIsOpenDate] = useState(false)

  const [month, setMonth] = useState(0)
  const [selectedDate, setSelectedDate] = useState(0)
  const [money, setMoney] = useState("")

  const [myLoan, setMyLoan] = useState({})

  const JoinLoan = async () => {
    if (month && selectedDate && money) {
      await setMyLoan({
        loanProductId: params.id,
        amount: money,
        userId: store.userId,
        paymentMethod: 1,
        paymentDate: selectedDate,
        term: month
      })
    } else {
      Swal.fire({
        title: "내용을<br/>입력해주세요.",
        icon: "error",
        confirmButtonColor: "#1E7572",
        confirmButtonText: "확인",
      })
    }
  }

  const passwordInit = async (myLoan) => {
    const { value: password } = await Swal.fire({
      title: "지갑 비밀번호 입력",
      input: "password",
      inputPlaceholder: "지갑 비밀번호(6자리) 입력",
      inputAttributes: {
        maxlength: "6",
        autocapitalize: "off",
        autocorrect: "off"
      },
      confirmButtonColor: "#1E7572",
      confirmButtonText: "확인",
    });
    if (password) {
      const walletPassword = {
        walletPassword: password
      }
      walletPasswordCheck(
        walletPassword,
        res => {
          loanInfoStore.setOptionId(res)
          LoanJoin1(
            myLoan,
            response => {
              loanInfoStore.setOptionId(response)
              navigate(`/loan/${params.id}/joinInfo`, { state: { response } })
            },
            err => {
              console.error("1차 가입 실패", err)
            }
          )
        },
        err => {
          console.log("비밀번호 틀림", err)
          Swal.fire({
            title: "비밀번호를<br/>확인해주세요.",
            icon: "error",
            confirmButtonColor: "#1E7572",
            confirmButtonText: "확인",
          })
        }
      )
    } else {
      Swal.fire({
        title: "비밀번호를<br/>입력해주세요.",
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

  useEffect(() => {
    LoanDetail(
      params.id,
      res => {
        setFinancialProducts(res)
      },
      err => {
        console.log('대출 상품 상세 정보 가져오기 안됨', err);
      }
    )
  }, [])

  useEffect(() => {
    const goJoinInfo = () => {
      if (Object.keys(myLoan).length !== 0) {
        passwordInit(myLoan)
      }
    }

    if (myLoan) {
      goJoinInfo()
    }
  }, [myLoan])

  useEffect(() => {
    if (financialProducts && month && store.userType) {
      if (store.userType == "TRAINEE") {
        if (month == 6) {
          setFinalInterest(financialProducts.trainRate06)
        } else if (month == 12) {
          setFinalInterest(financialProducts.trainRate12)
        } else if (month == 24) {
          setFinalInterest(financialProducts.trainRate24)
        } else {
          console.log("수료생인데 이자는 얼마일까요?")
        }
      } else {
        if (month == 6) {
          setFinalInterest(financialProducts.farmerRate06)
        } else if (month == 12) {
          setFinalInterest(financialProducts.farmerRate12)
        } else if (month == 24) {
          setFinalInterest(financialProducts.farmerRate24)
        } else {
          console.log("농부인데 이자는 얼마일까요?")
        }
      }
    }
  }, [financialProducts, month])

  return (
    <div>
      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">대출기간</span>
          </div>
          <select className="input input-bordered selects w-full grayText" value={month} onChange={(event) => {
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
            <span className="label-text">대출금액</span>
          </div>
          <div className="input input-bordered fake-input">
            <input type="number" placeholder="금액을 입력해주세요." 
            value={money !== null ? money : ''}
            onChange={(event) => setMoney(event.target.value)} className="w-11/12" />
            <span>원</span>
          </div>
          <div className="label">
          </div>
        </label>
      </div>

      <div>
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">매월 납입일</span>
          </div>
          <div className="relative line mb-2 input input-bordered">
            <div
              type="text"
              className="input w-full h-8 fake-input"
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
            <span className="label-text">사용자유형</span>
          </div>
          {store.userType == "TRAINEE" && <div className="input input-bordered w-full fake-input" >귀농교육수료자</div>}
          {store.userType == "FARMER" &&
            <div className="input input-bordered w-full fake-input" >귀농인</div>}
        </label>
      </div>

      <div className="mt-3">
        <label className="form-control w-full">
          <div className="label">
            <span className="label-text">상환방법</span>
          </div>
          <div className="input input-bordered w-full  fake-input" >원리금균등분할상환</div>
        </label>
      </div>

      <div className="flex flex-col mt-10 mb-3 px-2">
        <div className="flex justify-between">
          <div className="middleText">
            총 월별 납입액
          </div>
          <div>
            {(Math.floor(Math.floor(money * finalInterest / 100) / month) + Math.floor(money / month)) ?
              <span className="largeText">{(Math.floor(Math.floor(money * finalInterest / 100) / month) + Math.floor(money / month)).toLocaleString()}</span> : 0}원
          </div>
        </div>

        <div className="divider"></div>

        <div className="flex justify-between mt-0">
          <div>
            월별 상환 금액(원금)
          </div>
          <div>
            {Math.floor(money / month) ? <span>{Math.floor(money / month).toLocaleString()}</span> : 0}원
          </div>
        </div>

        <div className="flex justify-between">
          <div>
            월별 상환 금액(이자)
          </div>
          <div>
            {Math.floor(Math.floor(money * finalInterest / 100) / month) ?
              <span>{Math.floor(Math.floor(money * finalInterest / 100) / month).toLocaleString()}</span> : 0}원
          </div>
        </div>
      </div>

      <div>
        <button className="savingJoinBtn w-full mt-5" onClick={() => JoinLoan()}>가입하기</button>
      </div>
    </div>
  )
}