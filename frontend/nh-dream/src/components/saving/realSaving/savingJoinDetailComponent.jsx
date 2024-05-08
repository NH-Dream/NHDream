import SavingJoinCard from "../element/savingJoinCard"
import SavingJoinData from "../element/savingJoinData"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function SavingJoinDetailComponent() {
  return (
    <div className="mx-3">
      <SavingJoinCard/>
      <SavingJoinData/>
    </div>
  )
}