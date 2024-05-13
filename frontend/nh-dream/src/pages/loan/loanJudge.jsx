import { useLocation } from "react-router-dom"
import { LoanJudgeComponent } from "../../components/loan/index"
import TopBar from "../../components/common/topBar"

export default function LoanJudgePage() {
  const location = useLocation()
  const { response } = location.state
  return (
    <div>
      <TopBar title="대출" />
      <LoanJudgeComponent response={response}/>
    </div>
  )
}