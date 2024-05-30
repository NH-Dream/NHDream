import LoanJoinCard from "./element/loanJoinCard"
import LoanJoinData from "./element/loanJoinData"
import { useParams } from "react-router"
import "../../assets/css/container.css"
import "../../assets/css/text.css"
import "../../assets/css/button.css"
import "../../assets/css/img.css"
import "../../assets/css/size.css"

export default function LoanJoinDetailComponent() {
  const params = useParams()
  return (
    <div className="mx-3">
      <LoanJoinCard params={params}/>
      <LoanJoinData/>
    </div>
  )
}