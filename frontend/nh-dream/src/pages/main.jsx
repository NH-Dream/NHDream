import { useStore } from "zustand"
import Logo from "../assets/images/main/logo.png"
import MyAsset from "../components/main/myasset"
import MyGoods from "../components/main/mygoods"
import { userStore } from '../stores/userStore'

export default function MainPage (){
  const { userName } = userStore()


  return(
    <div  style={{ maxWidth: '100vw', overflowX: 'hidden' }}>
      <div className="flex p-3 pb-0">
        <img src={Logo} alt="" style={{ width:60}}/>
      </div>
      <MyAsset />
      <div className="flex p-5 pt-3">
        <div className="font-bold text-2xl">{userName}</div>
        <div className="text-lg flex items-end">님 가입상품</div>
      </div>
      <MyGoods />
    </div>
  )
}