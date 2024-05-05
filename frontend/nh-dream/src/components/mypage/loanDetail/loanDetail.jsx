import LoanCard from "./element/card"
import LoanDetail from "./element/detail"

export default function LoanDetailComponent() {

  return (
    <div className="loanColor pt-3">
      <LoanCard />
      <LoanDetail />
    </div>
  )
}