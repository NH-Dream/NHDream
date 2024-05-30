import { Routes, Route } from "react-router-dom";
import { LoginPage, JudgingPage } from "./pages/user/login/index";
import { SignUpPage1, SignUpPage2, SignUpPage3, SignUpPage4, SignUpPage5Farmer, SignUpPage5Student } from "./pages/user/signUp/index"
import { ChangePasswordPage, FindPasswordPage } from "./pages/user/password";
import MainPage from './pages/main'
import MyPage from './pages/mypage'
import MyAccount from './components/mypage/myAccount'
import MyDepositHistoryPage from '@/components/mypage/myproduct/mydeposithistory'
import MySavingHistoryPage from '@/components/mypage/myproduct/mysavinghistory'
import MyWallet from './components/mypage/myWallet'
import { WalletTransfer1, WalletTransfer2, WalletTransfer3, WalletTransfer4, AutoTransfer1,AutoTransfer2,AutoTransfer3,AutoTransfer4} from './components/transfer'
import { LoanDetailPage, SavingDetailPage, DepositDetailPage, MyInfoPage, BeFarmerPage, ChangeWalletPasswordPage, ChangeMyPasswordPage } from './pages/myPage/index'
import { SavingMainPage, SavingJoinDetailPage, SavingJoinInfoPage, SavingJoinOkPage, DepositJoinDetailPage, DepositJoinInfoPage, DepositJoinOkPage } from "./pages/saving/index";
import ShopPage from './pages/shop'
import ItemDetail from "./components/shop/itemDetail"
import Order1 from "./components/shop/order1"
import Order2 from "./components/shop/order2"
import { LoanPage, LoanJoinDetailPage, LoanJoinInfoPage, LoanJudgePage, LoanJoinOkPage } from "./pages/loan/index"
import Calculator from "./components/loan/calculator";
import PrivateRoute from "./pages/private/privateRoute";
import AutoTransfer from "./components/mypage/auto/autoTransfer"
import IntegratedTransfer from "./components/mypage/integrated.jsx"
import AutoModify1 from "./components/transfer/automodify1"
import AutoModify2 from "./components/transfer/automodify2"
import AutoModify3 from "./components/transfer/automodify3"
import AutoModify4 from "./components/transfer/automodify4"
import Test from "@/pages/test"

export default function Router() {
  return(
    <Routes>
      {/* 로그인페이지 */}
      <Route path='/login' element={<LoginPage />}/>
      {/* 회원가입페이지 */}
      <Route path='/signUp/1' element={<SignUpPage1 />}/>
      <Route path='/signUp/2' element={<SignUpPage2 />}/>
      <Route path='/signUp/3' element={<SignUpPage3 />}/>
      <Route path='/signUp/4' element={<SignUpPage4 />}/>
      <Route path='/signUp/5/Farmer' element={<SignUpPage5Farmer />}/>
      <Route path='/signUp/5/Student' element={<SignUpPage5Student />}/>
      {/* 비밀번호찾기페이지 */}
      <Route path='/password/check' element={<FindPasswordPage />}/>  
      <Route path='/password/change' element={<ChangePasswordPage />}/>
      <Route element ={<PrivateRoute/>}>
        {/* 미인증회원대기페이지 */}
        <Route path='/waiting' element={<JudgingPage />}/>
        {/* 메인페이지 */}
        <Route path='/' element={<MainPage />} />
        {/* 마이페이지 */}
        <Route path='/mypage' element={<MyPage />} />
        {/* 회원정보페이지 */}
        <Route path='/mypage/myInfo' element={<MyInfoPage />} />
        {/* 비밀번호변경페이지 */}
        <Route path='/mypage/myInfo/changePassword' element={<ChangeMyPasswordPage />} />
        {/* 귀농인증페이지 */}
        <Route path='/mypage/myInfo/beFarmer' element={<BeFarmerPage />} />
        {/* 마이계좌 */}
        <Route path='/mypage/myaccount' element={<MyAccount />} />
        {/* 마이계좌-예금-상세거래페이지 */}
        <Route path='/mypage/myaccount/deposit/history/:id' element={<MyDepositHistoryPage />} />
        {/* 마이계좌-적금-상세거래페이지 */}
        <Route path='/mypage/myaccount/saving/history/:id' element={<MySavingHistoryPage />} />
        {/* 예금정보상세페이지 */}
        <Route path='/mypage/myaccount/deposit/:id' element={<DepositDetailPage />} />
        {/* 적금정보상세페이지 */}
        <Route path='/mypage/myaccount/saving/:id' element={<SavingDetailPage />} />
        {/* 대출상세페이지 */}
        <Route path='/mypage/myaccount/loan/:id' element={<LoanDetailPage />} />
        {/* 마이지갑 */}
        <Route path='/mypage/mywallet' element={<MyWallet />} />
        {/* 지갑비밀번호변경페이지 */}
        <Route path='/mypage/mywallet/changePassword' element={<ChangeWalletPasswordPage />} />
        {/* 지갑이체화면1 */}
        <Route path='/mypage/mywallet/1' element={<WalletTransfer1 />} />
        {/* 지갑이체화면2 */}
        <Route path='/mypage/mywallet/2' element={<WalletTransfer2 />} />
        {/* 지갑이체화면3 */}
        <Route path='/mypage/mywallet/3' element={<WalletTransfer3 />} />
        {/* 지갑이체화면4 */}
        <Route path='/mypage/mywallet/4' element={<WalletTransfer4 />} />
        {/* 자동이체화면1 */}
        <Route path='/auto/1' element={<AutoTransfer1 />} />
        {/* 자동이체화면2 */}
        <Route path='/auto/2' element={<AutoTransfer2 />} />
        {/* 자동이체화면3 */}
        <Route path='/auto/3' element={<AutoTransfer3 />} />
        {/* 자동이체화면4 */}
        <Route path='/auto/4' element={<AutoTransfer4 />} />
        {/* 예적금상품메인페이지 */}
        <Route path="/saving" element={<SavingMainPage />} />
        {/* 적금가입상세페이지 */}
        <Route path="/saving/:id" element={<SavingJoinDetailPage />} />
        {/* 적금가입정보확인페이지 */}
        <Route path="/saving/:id/join" element={<SavingJoinInfoPage />} />
        {/* 적금가입완료페이지 */}
        <Route path="/saving/:id/join/done" element={<SavingJoinOkPage />} />
        {/* 예금가입상세페이지 */}
        <Route path="/deposit/:id" element={<DepositJoinDetailPage />} />
        {/* 예금가입정보확인페이지 */}
        <Route path="/deposit/:id/join" element={<DepositJoinInfoPage />} />
        {/* 예금가입완료페이지 */}
        <Route path="/deposit/:id/join/done" element={<DepositJoinOkPage />} />
        {/* 대출상품페이지 */}
        <Route path='/loan' element={<LoanPage />} />
        {/* 대출납입계산기페이지 */}
        <Route path='/loan/cal' element={<Calculator />} />
        {/* 대출가입상세페이지 */}
        <Route path="/loan/:id" element={<LoanJoinDetailPage />} />
        {/* 대출가입정보확인페이지 */}
        <Route path="/loan/:id/joinInfo" element={<LoanJoinInfoPage />} />
        {/* 대출심사신청페이지 */}
        <Route path="/loan/:id/joinJudge" element={<LoanJudgePage />} />
        {/* 대출심사완료페이지 */}
        <Route path="/loan/joinJudge/done" element={<LoanJoinOkPage />} />
        {/* 상점메인화면 */}
        <Route path='/shop' element={<ShopPage />} />
        {/* 상점상품상세화면 */}
        <Route path='/shop/:itemId' element={<ItemDetail/>} />
        {/* 상점주문페이지1 */}
        <Route path='/shop/:itemId/1' element={<Order1/>} />
        {/* 상점주문페이지2 */}
        <Route path='/shop/:itemId/2' element={<Order2/>} />
        {/* 마이페이지-자동이체*/}
        <Route path='/mypage/auto' element={<AutoTransfer />} />
        {/* 마이페이지-통합거래내역*/}
        <Route path='/mypage/integrated' element={<IntegratedTransfer />} />
        {/* 자동이체변경하기*/}
        <Route path='/mypage/auto/modify/:id' element={<AutoModify1 />} />
        {/* 자동이체변경하기2*/}
        <Route path='/mypage/auto/modify2/:id' element={<AutoModify2 />} />
        {/* 자동이체변경하기3*/}
        <Route path='/mypage/auto/modify3/:id' element={<AutoModify3 />} />
        {/* 자동이체변경하기4*/}
        <Route path='/mypage/auto/modify4/:id' element={<AutoModify4 />} />
        {/* sse테스트*/}
        <Route path='/test' element={<Test />} />
      </Route>
    </Routes>
  )
}