import Back from '../../assets/images/topbar/back.png'
import SaeSsak from '../../assets/images/mypage/saessak.png'
import { useNavigate } from 'react-router-dom'
import { useEffect,useState } from 'react'
import { checkVoucherBalance } from "@/services/voucher"
import { checkUser } from "@/services/user"

export default function TopBar({ showBackIcon = false }){
  const navigate = useNavigate()
  const [loginId,setLoginId] = useState(null)
  const [dreamToken,setDreamToken] = useState(null)
  

  const handleBack = ()=>{
    navigate(-1);
  }

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
          // console.log('드림토큰 잔액 조회')
          setDreamToken(res.amount)
        },
        err => {
          console.log(err);
        }
      );
    }
  }, [loginId]);

  const formatCurrency = (amount) => {
    const formattedAmount = new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: 'KRW',
      currencyDisplay: 'symbol'
    }).format(amount);
    return `${formattedAmount.replace('₩', '')}`;
  }

  return(
    <div className='flex justify-between p-5'>
      <div className='flex'>
        {showBackIcon && (
        <img src={Back} alt="" style={{width:15}} onClick={handleBack}/>
        )}
        <h1 className='text-xl ml-3' style={{ fontFamily: 'Pretendard ExtraBold', fontWeight: 800 }}>드림상점</h1>
      </div>
      <div className='flex text-md items-center mr'>보유드림 
          <h1 className='font-bold ml-5'> {formatCurrency(dreamToken)}</h1>
          <img src={SaeSsak} alt="" />
        </div>
    </div>
  )
}