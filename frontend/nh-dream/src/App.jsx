import Router from "./router"
import BottomBar from "./components/common/BottomBar"
import "./assets/css/app.css"
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";

function App() {

  return (
    <div className="app-container">
      <div className="content">
        <Router />
      </div>
      <div className="bottom-bar">
        <BottomBar />
      </div>
  </div>
  )
}

export default App
