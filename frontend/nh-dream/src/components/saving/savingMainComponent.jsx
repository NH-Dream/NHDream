import SavingCalculatorComponent from "./element/savingCalculator"
import SavingMainCardComponent from "./element/savingMainCardComponent"
import "../../assets/css/container.css"
import "../../assets/css/text.css"
import "../../assets/css/button.css"
import "../../assets/css/img.css"
import "../../assets/css/size.css"

export default function SavingMainComponent() {
  return (
    <div className="mx-3">
      <SavingCalculatorComponent/>
      <SavingMainCardComponent/>
    </div>
  )
}