import TopBar from "../../common/topBar"
import { useNavigate } from "react-router-dom"
import AutoHistory from "./autoHistory"

export default function AutoTransfer(){
  const navigate = useNavigate()


  return(
    <div>
      <TopBar />
      <div className="p-5">
        <button className="btn w-full text-lg" style={{ backgroundColor:'rgb(88, 193, 189, 0.5)'}}
        onClick={()=>navigate('/auto/1')}>자동이체등록하기</button>
      </div>
      <div className="font-bold p-3 text-xl" style={{ borderBottom: '1px solid #ccc' }}>
        자동이체내역
      </div>
      < AutoHistory />

    </div>
  )
}