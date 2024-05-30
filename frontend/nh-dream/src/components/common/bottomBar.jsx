import { useState,useEffect } from 'react'
import Home from '../../assets/images/bottombar/home.png'
import HomeC from '../../assets/images/bottombar/homeclick.png'
import Shop from '../../assets/images/bottombar/shop.png'
import ShopC from '../../assets/images/bottombar/shopclick.png'
import Loan from '../../assets/images/bottombar/money.png'
import LoanC from '../../assets/images/bottombar/moneyclick.png'
import User from '../../assets/images/bottombar/user.png'
import UserC from '../../assets/images/bottombar/userclick.png'
import deposit from '../../assets/images/bottombar/deposit.png'
import depositC from '../../assets/images/bottombar/depositclick.png'
import { Link,useLocation } from 'react-router-dom'
import '../../assets/css/bottombar.css'


export default function BottomBar(){
  const [currentPage, setCurrentPage] = useState('')
  const location = useLocation()

  useEffect(() => {
    const path = location.pathname.split('/')[1];
    setCurrentPage(path);
  }, [location]);

  const menuItems = [
    { key: 'deposit', to: '/saving', text: '예/적금', image: currentPage.startsWith('saving') ? depositC : deposit },
    { key: 'loan', to: '/loan', text: '대출', image: currentPage.startsWith('loan') ? LoanC : Loan},
    { key: 'home', to: '/', text: '홈', image: currentPage === '' ? HomeC : Home },
    { key: 'shop', to: '/shop', text: '상점', image: currentPage.startsWith('shop') ? ShopC : Shop },
    { key: 'mypage', to: '/mypage', text: '마이', image: currentPage.startsWith('mypage') ? UserC : User },
  ]

  return(
    <div className="flex justify-between p-2 px-4 pb-7"
    style={{
      borderTopRightRadius: '10px',
      borderTopLeftRadius: '10px',
      borderWidth: '2px',
      borderStyle: 'solid',
      backgroundColor: 'rgba(255, 255, 255, 0.7)', 
      boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)', 
      backdropFilter: 'blur(5px)', 
      WebkitBackdropFilter: 'blur(10px)'
    }}>
    {menuItems.map(({ key, to, text, image }) => (
      <div key={key} className='flex flex-col items-center justify-center'>
        <Link to={to}>
          <div className='flex justify-center'><img src={image} alt="메뉴바이미지" style={{ width:30,height:30}} /></div>
          <p className='text-center text-xs' style={{ color:'#bbbbbb'}}>{text}</p>
        </Link>
      </div>
    ))}
    </div>
  )
}