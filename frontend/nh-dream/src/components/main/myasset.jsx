import { useState,useEffect } from "react"
import { useNavigate } from "react-router-dom";
import Coin from "../../assets/images/main/coin.png"
import SaeSsak from "../../assets/images/mypage/saessak.png"
import Slider from "react-slick";
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import { userStore } from "../../stores/userStore";
import { checkWalletBalance } from "@/services/wallet";
import { checkUser } from '@/services/user'
import { checkVoucherBalance } from '@/services/voucher'
import copyImg from '@/assets/images/copy.png'
import Swal from "sweetalert2"

import secureLocalStorage from "react-secure-storage";

export default function MyAsset(){
  const navigate = useNavigate()
  const [visible, setVisible] = useState(() => {

    const storedVisible = secureLocalStorage.getItem("visible");
    return storedVisible !== null ? storedVisible === "true" : true;
  });
  const { walletAddress } = userStore()
  const [walletBalance,setWalletBalance] = useState(null)
  const [loginId,setLoginId] = useState(null)
  const [dreamToken,setDreamToken] = useState(null)

  useEffect(() => {
    // 상태 visible이 변경될 때마다 로컬 스토리지 업데이트
    secureLocalStorage.setItem("visible", visible.toString());
  }, [visible]); 


  const handleVisible = () => {
    const newVisible = !visible;
    setVisible(newVisible);
    secureLocalStorage.setItem("visible", newVisible);
  };

  const settings = {
    infinite: true,
    speed: 500, 
    slidesToShow: 1, 
    slidesToScroll: 1
  }

  useEffect(()=>{
    checkWalletBalance(
      res =>{
        // console.log(res)
        setWalletBalance(res.balance)
        },
      err =>{console.log(err)}
    )
  },[])

  useEffect(() => {
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
          //  console.log('드림토큰 잔액 조회')
          setDreamToken(res.amount)
        },
        err => {
          console.log(err);
        }
      );
    }
  }, [loginId]);

  const formatCurrency = (amount,currencySuffix) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}${currencySuffix}`
  }


  function formatAddress(address) {
    const visiblePart = address.slice(0, 33); 
    return `${visiblePart}...`;             
  }

  const moreBtn = () => {
    navigator.clipboard.writeText(walletAddress)
    Swal.fire({
      icon:'success',
      html: "<b>나의 지갑주소가 복사되었습니다</b>",
      showCloseButton: true,
      showConfirmButton: false
    })
  }

  return(
    <Slider {...settings}>
    <div className="p-3 pt-0">
      <div className="p-3 rounded-lg shadow-lg"
        style={{
          background: "linear-gradient(to right, #9fecc9 0%, #73e0ae 25%, #67d49d 50%)",
          minHeight: '210px'
        }}>
          <div className="flex">
            <img src={Coin} alt="" />
            <div className="font-bold ml-2">NHDC</div>
          </div>
          <div className="flex">
            <div className="text-sm">{ formatAddress(walletAddress) }</div>
            <div><img src={copyImg} alt="복사하기" onClick={moreBtn}/></div>
          </div>
          <div></div>
          <div className="flex pt-10">
            <div className="font-bold text-xl">{ visible?  formatCurrency(walletBalance,'원') : '잔액 숨김' }</div>
            <button className="btn btn-sm ml-2" onClick={handleVisible}>
              { visible? '숨김' : '보기' }
            </button>
          </div>
          <div className="flex justify-between w-full pt-5">
            <button className="btn btn-md flex-grow mx-2 text-lg" onClick={()=>navigate('/mypage/mywallet')}>조회</button>
            <button className="btn btn-md flex-grow mx-2 text-lg" onClick={()=>navigate('/mypage/mywallet/1')}>송금</button>
          </div>
      </div>     
    </div>

    <div className="p-3 pt-0">
      <div className="p-3 rounded-lg shadow-lg"
        style={{
          background: "linear-gradient(to right, #E8F8B7 0%, #D6F394 50%, #C4EF7D 100%)",
          minHeight: '210px'
        }}>
          <div className="flex">
            <img src={SaeSsak} alt="" />
            <div className="font-bold ml-2">My dream</div>
          </div>
         
          <div className="flex pt-16">
            <div className="font-bold text-xl">{formatCurrency(dreamToken,'드림')}</div>
          </div>
          <div className="flex justify-between w-full pt-6">
            <button className="btn btn-md text-lg flex-grow mx-2"
            onClick={()=>navigate('/shop')}>상점가기</button>
          </div>
      </div>     
    </div>
    </Slider>
  )
}