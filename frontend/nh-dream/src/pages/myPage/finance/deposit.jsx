import { DepositDetailComponent } from "../../../components/mypage/savingSearch/index"
import TopBar from "../../../components/common/topBar";

export default function DepositDetailPage() {
  return (
    <div>
      <TopBar title="예금상세정보" color="#FBFBB7"/>
      <DepositDetailComponent />
    </div>
  )
}