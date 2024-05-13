import { useLocation } from "react-router-dom"
import { LoanJoinInfoComponent } from "../../components/loan/index"
import TopBar from "../../components/common/topBar"

export default function LoanJoinInfoPage() {
  const location = useLocation()
  const { response } = location.state
  return (
    <div>
      <TopBar title="대출" />
      <LoanJoinInfoComponent response={response}/>
      {/* <LoanJoinInfoComponent/> */}
    </div>
  )
}