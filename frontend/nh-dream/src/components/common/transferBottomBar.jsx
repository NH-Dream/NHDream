import "../../assets/css/app.css"


export default function TransferBottomBar({ 
  next,
  showCancel = true,
  cancelClick,
  buttonText='다음',
  backgroundColor = 'rgba(196,239,125,0.8)'}){

  
  return(
    <div className="grid grid-cols-12 bottom-bar h-20">
      { showCancel && cancelClick && (
        <div className="col-span-5 flex justify-center items-center font-bold text-xl" 
             onClick={cancelClick}
             style={{ backgroundColor:'white'}}>취소</div>
      )}
      <div className={`flex justify-center items-center font-bold text-xl ${showCancel ? 'col-span-7' : 'col-span-12'}`}
        style={{ backgroundColor: backgroundColor}}
        onClick={next}>{buttonText}</div>
    </div>
  )
}