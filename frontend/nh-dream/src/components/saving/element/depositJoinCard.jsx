import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { DepositDetail } from "@/services/deposit"
import moreBar from "../../../assets/images/moreBar.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositJoinCard() {
  const params = useParams()
  const [isOpen, setIsOpen] = useState(false)
  const clickChange = (imOpen) => {
    setIsOpen(imOpen);
  };

  const [financialProducts, setFinancialProducts] = useState([])
  const makeMoney = (money) => {
    if (typeof money === 'number' && !isNaN(money)) {
      return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }}
  useEffect(() => {
    DepositDetail(
      params.id,
      res => {
        setFinancialProducts(res)
      },
      err => {
        console.log('예금 상세정보 가져오기 안됨', err);
      }
    )
  }, [])

  return (
    <div className="card card-compact savingColor pt-3">

      <div className="flex flex-col ml-3">
        <div className="largeText mb-2">
          {financialProducts?.name}
        </div>
        <div className="flex justify-between">
          <div className="flex flex-col">
            <div>
              금리(단리)
            </div>
            <div className="largeText">
              {financialProducts?.minRate} ~ {financialProducts?.maxRate}%
            </div>
          </div>
          <div className="flex flex-col justify-end pr-3">
            <div>
              ONLY
            </div>
            <div>
              만기 시 자동해지
            </div>
          </div>
        </div>
      </div>


      <div className="collapse mainColor mt-5">
        <input type="checkbox" checked={isOpen} onChange={(event) => clickChange(event.target.checked)} />
        <div className="collapse-title  whiteColor pr-3 text-xl font-medium middleText flex justify-between">
          {isOpen === false ? (<div className="flex">
            <div className="mr-3">
              최대 납입액
              <p className="largeText">
                {makeMoney(financialProducts?.maximum)}원
              </p>
            </div>
            <div className="ml-3">
              납입개월
              <p className="largeText">
                6 / 12 / 24
              </p>
            </div>
          </div>) : (
            <div>
              금리상세
            </div>
          )}
          <img src={moreBar} alt="하단아이콘" className="w-6 h-3 mt-2" />
        </div>
        <div className="collapse-content">
          {/* 표 */}
          <div className="overflow-x-auto bg-white rounded-xl">
            <table className="table table-pin-rows table-pin-cols">
              {/* head */}
              <thead>
                <tr>
                  <th></th>
                  <th>6개월</th>
                  <th>12개월</th>
                  <th>24개월</th>
                </tr>
              </thead>
              <tbody>
                {/* row 1 */}
                <tr>
                  <th>수료자</th>
                  <td>{financialProducts?.graduateRate6Months}%</td>
                  <td>{financialProducts?.graduateRate12Months}%</td>
                  <td>{financialProducts?.graduateRate24Months}%</td>
                </tr>
                {/* row 2 */}
                <tr>
                  <th>귀농인</th>
                  <td>{financialProducts?.farmerRate6Months}%</td>
                  <td>{financialProducts?.farmerRate12Months}%</td>
                  <td>{financialProducts?.farmerRate24Months}%</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}