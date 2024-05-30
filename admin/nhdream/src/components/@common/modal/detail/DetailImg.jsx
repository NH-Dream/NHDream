import { useNavigate } from "react-router-dom";

const DetailImg = ({label, imgAddress}) => {
    
    return(
        <div className='flex border-b-2 items-center justify-between mb-3 last:mb-1'>
            <div className="my-1 font-bold">{label}</div>
                <button 
                    style={{ minHeight: 'auto' }} 
                    className="btn btn-neutral mr-2 h-6"
                    onClick={()=>document.getElementById('my_modal_1').showModal()}
                >
                    확 인
                </button>
                <dialog id="my_modal_1" className="modal">
                    <div className="modal-box">
                        <img src={imgAddress}></img>
                        <div className="modal-action">
                            <form method="dialog">
                                {/* if there is a button in form, it will close the modal */}
                                <button className="btn">Close</button>
                            </form>
                        </div>
                    </div>
                </dialog>
        </div>
    )
}

export default DetailImg; 