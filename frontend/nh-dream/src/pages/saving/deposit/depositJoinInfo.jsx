import { useLocation } from "react-router-dom"
import TopBar from "../../../components/common/topBar"
import { DepositJoinInfoComponent } from "../../../components/saving/index"

export default function DepositJoinInfoPage() {
  const location = useLocation()
  const { myDeposit } = location.state
  return (
    <div>
      <TopBar title="예금" />
      <DepositJoinInfoComponent myDeposit={myDeposit}/>
    </div>
  )
}