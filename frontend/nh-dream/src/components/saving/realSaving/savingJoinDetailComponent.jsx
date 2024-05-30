import { useParams } from "react-router-dom"
import SavingJoinCard from "../element/savingJoinCard"
import SavingJoinData from "../element/savingJoinData"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function SavingJoinDetailComponent() {
  const params = useParams()
  return (
    <div className="mx-3">
      <SavingJoinCard params={params}/>
      <SavingJoinData/>
    </div>
  )
}