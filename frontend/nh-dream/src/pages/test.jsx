import { useEffect, useState } from "react"
import { ToastContainer, toast } from 'react-toastify'
import { EventSourcePolyfill } from 'event-source-polyfill'
import 'react-toastify/dist/ReactToastify.css';
import { userStore } from "@/stores/userStore";
import { test } from "@/services/test";
import "@/assets/css/sse.css"
import secureLocalStorage from "react-secure-storage";


export default function Test(){
  const { userId } = userStore()
  const token = secureLocalStorage.getItem('access')

  const EventSource = EventSourcePolyfill

  useEffect(() => {
    console.log(token)
    if (!token) {
      console.log('로그인이 필요합니다')
      return;
    }
    
    const eventSource = new EventSource(`https://k10s209.p.ssafy.io/api/sse/subscribe?userId=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      heartbeatTimeout: 86400000,
    });
    // console.log(eventSource)
    eventSource.onopen = () => {
      console.log('SSE연결성공')
    }

    eventSource.onerror = error => {
      console.error("SSE 연결 오류:", error);
      eventSource.close();
    };

    eventSource.addEventListener('test', event => {
      const data = JSON.parse(event.data)
      console.log(data)
      const toastMsg = `${data.message}입니다`
      toast(toastMsg,{
        className:'withdrawal-toast',
        hideProgressBar: true,
        pauseOnHover: false,
        autoClose: 3000,
      })
    })

  


    return () => {
      eventSource.close();
    };
  }, [token]);
  
  const handleTest = () => {
    test(
      userId,
      res => {
        console.log(res)
      },
      err => {
        console.log(err)
      }
    )
  }

  return(
    <div>
      SSE 테스트 페이지
      <button className="btn" onClick={handleTest}>테스트버튼</button>
      <ToastContainer />
    </div>
  )
}
