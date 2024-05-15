import Back from '../../assets/images/topbar/back.png'
import { useNavigate } from 'react-router-dom'

export default function TopBar({ title, color,showBackIcon = true}){
  const navigate = useNavigate()

  const handleBack = ()=>{
    navigate(-1);
  }

  return(
    <div className='flex p-5 ' style={{ backgroundColor:color }}>
      {showBackIcon &&(
      <img src={Back} alt="" style={{width:15}} onClick={handleBack}/>
      )}
      <h1 className='font-bold text-xl ml-5'>{title}</h1>
    </div>
  )
}