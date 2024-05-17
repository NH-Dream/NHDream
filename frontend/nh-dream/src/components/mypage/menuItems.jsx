export default function MenuItems({ icon, title, onClick }){
  const isAutoTransfer = icon === 'AutoTransfer'
  
  return(
    <div className="p-3  shadow-md rounded-xl flex h-20" onClick={onClick}>
      <div className="flex items-center"><img src={icon} alt={title} className="" style={{ width:35,height:35}}/></div>
      <div className="flex items-center text-lg font-bold ml-5">{title}</div>
    </div>
  )
}