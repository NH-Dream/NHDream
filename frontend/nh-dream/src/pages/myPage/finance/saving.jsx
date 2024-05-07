import { SavingDetailComponent } from "../../../components/mypage/savingSearch/index"
import TopBar from "../../../components/common/topBar";

export default function SavingDetailPage() {
  return (
    <div>
      <TopBar title="예/적금 조회" color="#C8F0C5"/>
      <SavingDetailComponent />
    </div>
  )
}