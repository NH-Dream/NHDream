import "../../assets/css/shop.css"
import { useNavigate,useLocation, useParams } from "react-router-dom"
import TopBar from "./topBar"
import { useState,useEffect } from "react"
import { userStore } from "@/stores/userStore"
import { confrimWalletPassword } from "@/services/wallet"
import Swal from "sweetalert2"  
import { checkWalletBalance } from "@/services/wallet"
import { checkVoucherBalance,buyVoucherProduct } from "@/services/voucher"
import { checkUser } from "@/services/user"


export default function Order1(){
  const { userName } = userStore()
  const { userPhone } = userStore()
  const params = useParams()
  const itemId = params.itemId
  const navigate = useNavigate()
  const location = useLocation()
  const { count, item, totalPrice } = location.state 
  const [password,setPassword] = useState(null)
  const [walletBalance,setWalletBalance] = useState(null)
  const [usePoint,setUsePoint] = useState(null)
  const [loginId,setLoginId] = useState(null)
  const [dreamToken,setDreamToken] = useState(null)

  const successOrder = () =>{
    const finalPrice = totalPrice - usePoint;

    // 지갑 잔액 확인
    if (walletBalance < finalPrice) {
      document.getElementById('modal').close()
      Swal.fire({
        icon: "error",
        html: "<b>지갑 잔액이 부족합니다.</b>",
        showCloseButton: true,
        showConfirmButton: false
      });
      return; // 지갑 잔액 부족 시 종료
    }
    confrimWalletPassword(
      {
        walletPassword:password
      },
      res=>{
      try {
        // console.log(parseInt(usePoint))
        buyVoucherProduct({
            loginId,
            itemId:parseInt(itemId),
            amount: count,
            usingNHDC: totalPrice - usePoint,
            usingDRDC: parseInt(usePoint)
          },
          res => {
            // console.log(res);
            // console.log('상품결제성공')
            navigate(`/shop/${itemId}/2`,{ state: { count, item, totalPrice,usePoint } })
          },
          err => {
            console.log(err);
            throw new Error('상품 구매 중 오류 발생')
          }
        );
      } catch (error) {
        console.error('처리 중 오류 발생:', error);
      }
      },
      err=>{
        document.getElementById('modal').close();
        Swal.fire({
          icon: "error",
          html: "<b>지갑비밀번호가 일치하지 않습니다</b>",
          showCloseButton: true,
          showConfirmButton: false
        })
      }
    )  
  }

  
  useEffect(() => {
    // 지갑잔액조회
    checkWalletBalance(
      res => {
        setWalletBalance(res.balance);
      },
      err => {
        console.log(err);
      }
    );
     // 회원정보조회해서 아이디가져오기
    checkUser(
      res => {
        setLoginId(res.loginId);
      },
      err => {
        console.log(err);
      }
    );
  }, []);

  useEffect(() => {
    if (loginId) {
      // 바우처 잔액 조회
      checkVoucherBalance(
        loginId,
        res => {
          // console.log(res);
          // console.log('드림토큰 잔액 조회')
          setDreamToken(res.amount)
        },
        err => {
          console.log(err);
        }
      );
    }
  }, [loginId]);

  // 드림포인트 전부사용하기
  const usingAll = () =>{
    const maxUsePoint = Math.min(dreamToken, totalPrice); // 주문 금액과 드림토큰 잔액 중 작은 금액 사용
    setUsePoint(maxUsePoint);
  }

  // 드림포인트 입력 처리 함수
  const handleDreamPointChange = (e) => {
    let inputPoints = parseInt(e.target.value, 10); 
    if (isNaN(inputPoints) || inputPoints < 0) {
      setUsePoint(0); // 음수 또는 잘못된 입력 처리
    } else {
      const maxPoints = Math.min(dreamToken, totalPrice); 
      if (inputPoints > maxPoints) {
        setUsePoint(maxPoints); // 입력값이 최대 사용 가능 포인트보다 많으면 최대치로 설정
      } else {
        setUsePoint(inputPoints);
      }
    }
  };


  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}원`;
  }

  return(
    <>
    <TopBar showBackIcon={true}/>
    <div className="p-3">
      <div className="order-detail mb-2">
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">이름</h1> 
          <h1 style={{ flex : 7}} className="content1">{ userName }</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">연락처</h1> 
          <h1 style={{ flex : 7}} className="content1">{ userPhone }</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">배송지</h1> 
          <h1 style={{ flex : 7}} className="content1">경상북도 구미시 진평동</h1>
        </div>
      </div>
      <div className="order-detail mb-2">
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">상품명</h1> 
          <h1 style={{ flex : 7}} className="content1">{item.title}</h1>
        </div>
        <div className="flex p-2">
          <h1 style={{ flex : 3}} className="title1">총수량</h1> 
          <h1 style={{ flex : 7}} className="content1">{count}개</h1>
        </div>
      </div>
      <div className="order-detail mb-5">
        <div className="flex p-2">
          <h1 style={{ flex : 4}} className="title1">총 상품금액</h1> 
          <h1 style={{ flex : 6}} className="content1">{formatCurrency(totalPrice)}</h1>
        </div>
        <div className="flex p-2 pb-0">
          <h1 style={{ flex : 4}} className="title1">배송비</h1> 
          <h1 style={{ flex : 6}} className="content1">0원</h1>
        </div>
        <div className="p-2">
          <div style={{  borderBottom: '1px solid gray' }} className=""></div>
        </div>
        <div className="p-2 pb-0">
          <h1 className="title1 mb-2">드림포인트</h1>
          <div className="flex">
            <input type="number" placeholder="사용포인트입력" 
            className="input input-bordered w-full max-w-xs h-8"
            value={usePoint}
            onChange={handleDreamPointChange}/>
            <button className="btn btn-sm ml-1 bg-[#ccc]"
            onClick={usingAll}>모두사용</button>
          </div>
          <div className="ml-2 mt-1">보유드림 {formatCurrency(dreamToken)}</div>
        </div>
        <div className="flex p-2 pb-0">
          <h1  className="title1" style={{ flex : 4}} >지갑잔액</h1> 
          <h1 style={{ flex : 6}} className="content1">{formatCurrency(walletBalance)}</h1>
        </div>
        <div className="p-2">
          <div style={{  borderBottom: '1px solid gray' }} className=""></div>
        </div>
        <div className="flex p-2">
          <div className="content1 text-xl" style={{ flex : 5}} >최종결제금액</div>
          <div className="content1 text-red-500" style={{ flex : 5}} >{formatCurrency(totalPrice-usePoint)}</div>
        </div>
      </div>
      <button className="btn btn-lg w-full text-lg bg-[#67D49D] text-white"
      onClick={()=>document.getElementById('modal').showModal()}>
        결제하기
        </button>
        <dialog id="modal" className="modal">
          <div className="modal-box">
            <form method="dialog" className="flex justify-end">
              <button className="font-bold">x</button>
            </form>
            <h3 className="font-bold text-lg mb-5">지갑 비밀번호 입력</h3>
            <input type="password" 
            className="input input-bordered w-full max-w-xs mb-2"
            onChange={(e)=>setPassword(e.target.value)}/>
            <button className="btn w-full text-lg bg-[#67D49D] text-white"
            onClick={successOrder}
            >
            확인
            </button>
          </div>
        </dialog>
    </div>
    </>
  )
}