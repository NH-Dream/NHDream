import DepositJoinCard from "./element/depositJoinCard"
import DepositJoinData from "./element/depositJoinData"
import "../../assets/css/container.css"
import "../../assets/css/text.css"
import "../../assets/css/button.css"
import "../../assets/css/img.css"
import "../../assets/css/size.css"

export default function DepositJoinDetailComponent() {
  return (
    <div className="mx-3">
      <DepositJoinCard/>
      <DepositJoinData/>
    </div>
  )
}