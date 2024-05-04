export default function MenuItems({ icon, title, onClick }){
  return(
    <div className="p-3 shadow-md rounded-xl flex h-20" onClick={onClick}>
      <div className="flex items-center"><img src={icon} alt={title} className="mt-2"/></div>
      <div className="flex items-center text-lg font-bold ml-2">{title}</div>
    </div>
  )
}