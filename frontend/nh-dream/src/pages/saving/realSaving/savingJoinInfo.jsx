import { useLocation } from "react-router-dom"
import TopBar from "../../../components/common/topBar"
import { SavingJoinInfoComponent } from "../../../components/saving/index"

export default function SavingJoinInfoPage() {
  const location = useLocation()
  const { savingInfo } = location.state
  return (
    <div>
      <TopBar title="적금" />
      <SavingJoinInfoComponent savingInfo={savingInfo}/>
    </div>
  )
}