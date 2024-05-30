import { useNavigate } from 'react-router-dom'
import TopBar from '../components/common/topBar'
import MenuItem from '../components/mypage/menuItems'
import SaeSsak from '../assets/images/mypage/saessak.png'
import MyWallet from '../assets/images/mypage/wallet.png'
import MyAccount from '../assets/images/mypage/account.png'
import AutoTransfer from '../assets/images/mypage/auto.png'
import Setting from '../assets/images/mypage/setting.png'
import Integrated from '../assets/images/mypage/integrated.png'
import Alarm from '../assets/images/mypage/alarm.png'
import {logout} from "../services/user"
import {userStore} from "../stores/userStore"
import { useEffect,useState } from 'react'
import { checkUser } from '../services/user'
import { checkVoucherBalance } from '@/services/voucher'
import secureLocalStorage from 'react-secure-storage'


export default function MyPage (){
  const navigate = useNavigate()
  const user = userStore()
  const { userName } = userStore()
  const [loginId,setLoginId] = useState(null)
  const [dreamToken,setDreamToken] = useState(null)

  const handleItemClick = (path) => {
    navigate(path);
  };

  const movePage = () => {
    goLogout();
    
    navigate("/login")
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
    return `${formattedAmount.replace('₩', '')}드림`;
  }



  // 로그아웃
  const goLogout = () => {
    logout(
      response => {
        console.log("로그아웃 성공")
        secureLocalStorage.removeItem("access")
        user.logout()
        navigate("/login");
      },
      error => {
        console.log("로그아웃 실패")
      },
    );
  }

  return(
    <div >
      <TopBar title="마이페이지" color="rgba(196, 239, 125, 0.5)" showBackIcon={false}/>
      <div className='h-40 p-5' style={{ backgroundColor:"rgba(196, 239, 125, 0.5)" }}>
        <div className='flex justify-between pt-3'>
          <div className='flex text-xl'><h1 className='font-bold'>{userName}</h1>님, 안녕하세요</div>
          <button 
            className="btn btn-sm bg-transparent border border-black rounded-full text-gray-700 hover:bg-[rgba(46,177,173,0.3)]"
            onClick={() => {movePage()}}
          >
            로그아웃
          </button>
        </div>
        <div className='flex justify-between pt-5 text-lg'>
          <div className='flex'>
            <img src={SaeSsak} alt="" />
            <h1 className='font-bold ml-3'> {formatCurrency(dreamToken)}</h1>
          </div>
          {/* <div className='flex'>
            <img src={Alarm} alt="" />
            <h1 className='font-bold'>알림함</h1>
          </div> */}
        </div>
      </div>
      <div className='p-2'>
        <MenuItem icon={MyWallet} title="마이지갑" onClick={() => handleItemClick('/mypage/mywallet')}/>
        <MenuItem icon={MyAccount} title="마이계좌" onClick={() => handleItemClick('/mypage/myaccount')}/>
        <MenuItem icon={Integrated} title="통합거래내역" onClick={() => handleItemClick('/mypage/integrated')}/>
        <MenuItem icon={AutoTransfer} title="자동이체" onClick={() => handleItemClick('/mypage/auto')}/>
        <MenuItem icon={Setting} title="개인정보조회/변경" onClick={() => handleItemClick('/mypage/myInfo')}/>
      </div>
    </div>
  )
}