import { LoanDetailComponent } from "../../../components/mypage/loanDetail/index"
import TopBar from "../../../components/common/topBar";

export default function LoanDetailPage() {
  return (
    <div>
      <TopBar title="대출상세정보" color="#BCD6D5"/>
      <LoanDetailComponent />
    </div>
  )
}