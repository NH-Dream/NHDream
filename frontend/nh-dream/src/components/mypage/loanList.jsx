import { useNavigate } from "react-router-dom"
import { checkMyloan, checkMyloanDetail } from "@/services/myproduct"
import { useEffect,useState } from "react"
import { userStore } from "@/stores/userStore"


export default function MyLoanList(){
  const navigate = useNavigate()
  const { userId } = userStore()
  const [myloanList,setMyloanList] = useState([])

  const handleClick = (item) =>{
    if (item.approval === 1){
      goLoanDetail(item.id)
    }
  }

  const goLoanDetail = (id) =>{
    navigate(`/mypage/myaccount/loan/${id}`)
  }
  

  useEffect(()=>{
    checkMyloan(
      userId,
      res=>{
        setMyloanList(res)
      },
      err=>{
        console.log(err)
      }
    )

  },[])

  const approvalStatus = {
    0: '승인거부',
    1: '승인',
    2: '대출심사중'
  };


  return(
    <div className="p-3 pt-0">
      {myloanList.map((item) => (
        <div key={item.id} className="shadow-md rounded-xl p-3 mt-3" style={{ border: '2px solid #ccc' }}
        onClick={()=>handleClick(item)}>
          <div className="flex justify-between">
            <div className="text-lg font-bold">{item.name}</div>
            <div>
              <div className="text-center">신청일자</div>
              <div>{item.date.split('T')[0]}</div>
            </div>
          </div>  
          <div className="flex justify-end mt-10">
            <div className="text-lg font-bold "
            style={item.approval === 1 ? { color: '#1E7572' } : {}}
            >{approvalStatus[item.approval]}</div>
          </div>
        </div>
      ))}
    </div>
  )
}