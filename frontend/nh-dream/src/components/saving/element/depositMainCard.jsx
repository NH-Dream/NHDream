import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { DepositList } from "@/services/deposit"
import moreBar from "../../../assets/images/moreBar.png"
import helpFarmer from "../../../assets/images/helpFarmer.png"
import farmerImg from "../../../assets/images/farmerImg.png"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositMainCardComponent() {
  const navigate = useNavigate()
  const [openId, setOpenId] = useState(null)
  const clickChange = (event, id) => {
    setOpenId(event.target.checked ? id : null)
  };
  const makeMoney = (money) => {
    return money.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }
  const movePage = (id) => {
    navigate(`/deposit/${id}`)
  }
  const [financialProducts, setFinancialProducts] = useState([])
  
  useEffect(() => {
    DepositList(
      res => {
        setFinancialProducts(res)
      },
      err => {
        console.log('예금 목록 가져오기 안됨', err);
      }
    )
  }, [])

  return (
    <div>
      { financialProducts.map((product, index) => (
      <div key={index} className={`collapse cardColor mt-5 ${openId === product.id ? 'open' : ''}`}>
        <input type="checkbox" checked={openId === product.id} onChange={(event) => clickChange(event, product.id)} />
        <div className="collapse-title pr-3 text-xl font-medium middleText flex justify-between">
          <div className="flex flex-col whiteColor">
            <div>
              <img src={helpFarmer} alt="귀농지원" />
            </div>
            <div className="largeText mt-3">
              <span>{product.name}</span>
            </div>
            {openId !== product.id && <div className="mt-3">
              최고 이자율 <span className="largeText">{Math.floor(product.maximumRate*10000)/100}</span>%
            </div>}
          </div>
          <div>
            <img src={moreBar} alt="하단아이콘"
              className="w-6 h-3 mt-2" />
          </div>
        </div>
        <div className="collapse-content">
          <div className="flex">
            <img src={farmerImg} alt="농부 이미지" className="w-1/3" />
            <div className="flex flex-col whiteColor items-center justify-center ml-2">
              <div>
                예치기간 : 6/ 12/ 24 개월
              </div>
              <div>
                최대금액 <span className="largeText">{makeMoney(product.maximumLimit)}</span>원
              </div>
              <div>
                최고 연(12개월) <span className="largeText">{Math.floor(product.maximumRate*10000)/100}%</span>
              </div>
            </div>
          </div>
          <div onClick={() => movePage(product.id)} className="userBtn2">
            <button>가입하기</button>
          </div>
        </div>
      </div>))}
    </div>
  )
}