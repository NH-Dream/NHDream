import Setting from "../../../assets/images/mypage/setting3.png";
import { useState,useEffect} from "react";
import Swal from "sweetalert2"
import "@/assets/css/app.css"
import { checkAutoHistory } from "@/services/auto";
import { useNavigate } from "react-router-dom";
import { terminateAutoTransfer } from "@/services/auto";

export default function AutoHistory() {
  const navigate = useNavigate()
  const [openDropdownIndex, setOpenDropdownIndex] = useState(null);
  const [autoHistoryItems,setAutoHistoryItems] = useState([])
  const [refreshKey, setRefreshKey] = useState(true)

  const toggleDropdown = (index) => {
    if (openDropdownIndex === index) {
      setOpenDropdownIndex(null);
    } else {
      setOpenDropdownIndex(index);
    }
  };
  
  useEffect(()=>{
    checkAutoHistory(
      0,
      50,
      res=>{
        // console.log(res)
        setAutoHistoryItems(res)
      },
      err=>{
        console.log(err)
      }
    )
  },[refreshKey])


  const goTerminate = (id) => {
    Swal.fire({
      html: "<b>정말 해지하시겠습니까?</b>",
      showCancelButton: true,
      confirmButtonColor: "#2EB1AD",  
      cancelButtonColor: "#ffffff",   
      confirmButtonText: "네",
      cancelButtonText: "아니오",
      reverseButtons: true,          
      customClass: {
        cancelButton: 'swal2-cancel'
      }
    }).then((result) => {
      if (result.isConfirmed) {
        terminateAutoTransfer(
          id,
          res => {
            // console.log(res)
            Swal.fire({
              html: "<b>해지가 완료되었습니다.</b>",
              icon: "success",
              showCloseButton: true,
              showConfirmButton: false
            });
            setRefreshKey(!refreshKey);  
          },
          err => {
            console.log(err);
            Swal.fire({
              title: '오류!',
              text: '해지 과정에서 오류가 발생했습니다.',
              icon: 'error',
              confirmButtonText: '확인'
            });
          }
        );
      }
    });
  }
  

  return (
    <>
      {autoHistoryItems.map((item, index) => (
        <div key={index} className="p-4" style={{ borderBottom: '1px solid #ccc' }}>
          { item.type === 0 && (
          <div className="flex justify-end relative" onClick={() => toggleDropdown(index)}>
            <img src={Setting} alt="" style={{ height: 15 }} />
            {openDropdownIndex === index &&  (
            <div className="bg-white shadow-md mt-2 absolute  min-w-[100px]"  style={{ right: 0,bottom:-90,zIndex: 1000 }}>
              <div className="p-2 hover:bg-gray-100 cursor-pointer" style={{ borderBottom: '1px solid #ccc' }}
              onClick={()=>navigate(`/mypage/auto/modify/${item.id}`)}>변경하기</div>
              <div className="p-2 hover:bg-gray-100 cursor-pointer"
              onClick={()=>goTerminate(item.id)}>
              해지하기</div>
            </div>
          )}
          </div>
          )}
          
          <div className="flex justify-between mt-5">
            <div>받는 분</div>
            <div className="font-bold">{item.recipientName}</div>
          </div>
          <div className="flex justify-between mt-2">
            <div>받는 계좌</div>
            <div className="font-bold">{item.recipientWalletAddress.slice(0, 10) + '...'}</div>
          </div>
          <div className="flex justify-between mt-2">
            <div>이체금액</div>
            <div className="font-bold">{item.amount ? `${item.amount.toLocaleString()}원` : ''}</div>
          </div>
          <div className="flex justify-between mt-2">
            <div>자동이체 기간</div>
            <div className="font-bold">{item.startedAt}~{item.expiredAt}</div>
          </div>
          <div className="flex justify-between mt-2">
            <div>자동이체일</div>
            <div className="font-bold">{item.transferDay}일</div>
          </div>
          <div className="flex justify-between mt-2">
            <div>다음이체일</div>
            <div className="font-bold">{item.nextScheduledTransferDate}</div>
          </div>     
        </div>
      ))}
    </>
  )
} 
