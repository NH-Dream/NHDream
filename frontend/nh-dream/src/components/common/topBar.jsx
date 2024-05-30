import Back from '../../assets/images/topbar/back.png'
import { useNavigate } from 'react-router-dom'

export default function TopBar({ title, color,showBackIcon = true}){
  const navigate = useNavigate()

  const handleBack = ()=>{
    navigate(-1);
  }

  return(
    <div className='flex p-4' style={{ backgroundColor:color }}>
      {showBackIcon &&(
      <img src={Back} alt="" style={{width:15}} onClick={handleBack}/>
      )}
      <h1 className='text-xl ml-5' style={{ fontFamily: 'Pretendard ExtraBold', fontWeight: 800 }}>{title}</h1>
    </div>
  )
}