import { useLocation } from "react-router-dom";
import Router from "./router"
import BottomBar from "./components/common/bottomBar"
import "./assets/css/app.css"
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import { useEffect } from "react";
import { ToastContainer, toast } from 'react-toastify'
import { EventSourcePolyfill } from 'event-source-polyfill'
import 'react-toastify/dist/ReactToastify.css';
import { userStore } from "@/stores/userStore";
import "@/assets/css/sse.css"
import secureLocalStorage from "react-secure-storage";

function App() {
  const location = useLocation()
  const token = secureLocalStorage.getItem('access')
  const { userId } = userStore()
  const EventSource = EventSourcePolyfill

  const shouldHideBottomBar = () => {
    const hidePatterns = [
      /^\/login$/,
      /^\/waiting$/,
      /^\/signUp\/\d+/,
      /^\/password\//,
      /\/mypage\/mywallet\/\d+$/,
      /\/auto\/\d+$/,
    ];
    const definedRoutes = [
      '/',
      '/login',
      '/waiting',
      /^\/signUp\//,
      /^\/password\//,
      '/mypage',
      '/mypage/myInfo',
      '/mypage/myaccount',
      '/mypage/mywallet',
      '/mypage/mywallet/1',
      '/mypage/mywallet/2',
      '/mypage/mywallet/3',
      '/mypage/mywallet/4',
      '/auto/1',
      '/auto/2',
      '/auto/3',
      '/auto/4',
      '/saving',
      "/saving/:id",
      "/saving/:id/join/done",
      "/deposit/:id",
      "/deposit/:id/join/done",
      '/shop',
      '/shop/:itemId',
      '/shop/:itemId/2',
      '/loan',
      '/loan/cal',
      '/mypage/auto'
    ];
    
    // 현재 경로가 숨겨야 하는 경로 중 하나와 일치하는지 확인
    const isHidePatternMatch = hidePatterns.some((pattern) => pattern.test(location.pathname));

    if (isHidePatternMatch) {
      return true;
    }

    // 정의된 경로를 확인할 때 사용할 정규 표현식으로 변환하는 함수
    const convertPathToRegex = (path) => {
      if (typeof path === 'string' && path.includes(':')) {
        const escapedPath = path.replace(/\//g, '\\/');
        const regexPath = escapedPath.replace(/:[^\s/]+/g, "([^\\s/]+)");
        return new RegExp(`^${regexPath}$`);
      } else if (typeof path === 'string') {
        return new RegExp(`^${path.replace(/\//g, '\\/')}\/?$`);
      }
      return path;
    };
    
    const isDefinedRoute = definedRoutes.some((route) => convertPathToRegex(route).test(location.pathname));

    return !isDefinedRoute;
  }


  // SSE 연결
  useEffect(() => {
    // console.log(token)
    if (!token) {
      console.log('로그인이 필요합니다')
      return;
    }
    
    const eventSource = new EventSource(`https://k10s209.p.ssafy.io/api/sse/subscribe?userId=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        Connetction: 'keep-alive',
      },
      heartbeatTimeout: 86400000,
    });

    eventSource.onopen = () => {
      console.log('SSE연결성공')
    }

    eventSource.onerror = error => {
      console.error("SSE 연결 오류:", error);
      eventSource.close();
    };

  //  입금 출금에 관한 이벤트 
   eventSource.addEventListener('dream', event => {
    const data = JSON.parse(event.data);
    console.log(data);
    const transactionType = data.trade === 0 ? "입금" : "출금";
    const toastClassName = data.trade === 0 ? "deposit-toast" : "withdrawal-toast"
    const formattedAmount = new Intl.NumberFormat('ko-KR').format(data.amount);
    const transactionDate = new Date(data.time);
    const formattedDate = `${transactionDate.getFullYear()}-${(transactionDate.getMonth() + 1).toString().padStart(2, '0')}-${transactionDate.getDate().toString().padStart(2, '0')}`;
    const formattedTime = transactionDate.toTimeString().split(' ')[0]; 

       
        const toastMsg = (
          <div>
            <div>{formattedDate} {formattedTime}</div>
            <div>[{transactionType}] {formattedAmount}원 {data.name}</div>
          </div>
        );

    toast(toastMsg, {
      className: toastClassName,
      hideProgressBar: true,
      pauseOnHover: false,
      autoClose: 4000,
    });
  });

  // 예/적금,대출 가입이벤트
  eventSource.addEventListener('product', event => {
    const data = JSON.parse(event.data);
    console.log(data); 
    const transactionDate = new Date(data.time);
    const formattedDate = `${transactionDate.getFullYear()}-${(transactionDate.getMonth() + 1).toString().padStart(2, '0')}-${transactionDate.getDate().toString().padStart(2, '0')}`;
    const formattedTime = transactionDate.toTimeString().split(' ')[0];
    let productType;
    switch(data.trade) {
        case 1:
            productType = "예금";
            break;
        case 2:
            productType = "적금";
            break;
        case 3:
            productType = "대출";
            break;
    }

    let productState;
    switch(data.state) {
        case 0:
          productState = "신청";
            break;
        case 1:
          productState = "심사완료";
            break;
        case 2:
          productState = "가입";
            break;
        case 3:
          productState = "만료";
          break;
    }

    const toastMsg = (
      <div>
        <div>{formattedDate} {formattedTime}</div>
        <div>{productType} 상품이 {productState} 되었습니다</div>
      </div>
    );


    toast(toastMsg, {
      className:'product-toast',
      hideProgressBar: true,
      pauseOnHover: false,
      autoClose: 4000,
    });
  })



    return () => {
      eventSource.close();
    };
  }, [token]);


  return (
    <div className="app-container">
      <div className="content">
        <ToastContainer />
        <Router />
      </div>
      {!shouldHideBottomBar() && (
      <div className="bottom-bar">
        <BottomBar />
      </div>)}
    </div>
  )
}

export default App
