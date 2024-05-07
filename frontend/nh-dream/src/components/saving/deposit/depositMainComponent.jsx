import DepositCalculatorComponent from "../element/depositCalculator"
import DepositMainCardComponent from "../element/depositMainCard"
import "../../../assets/css/container.css"
import "../../../assets/css/text.css"
import "../../../assets/css/button.css"
import "../../../assets/css/img.css"
import "../../../assets/css/size.css"

export default function DepositMainComponent() {
  return (
    <div className="mx-3">
      <DepositCalculatorComponent />
      <DepositMainCardComponent />
    </div>
  )
}