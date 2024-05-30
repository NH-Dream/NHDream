import { useState } from "react"
import Biryo from "../../assets/images/shop/biryo.png"
import BiryoClick from "../../assets/images/shop/biryoclick.png"
import Gigu from "../../assets/images/shop/gigu.png"
import GiguClick from "../../assets/images/shop/giguclick.png"
import Seed from "../../assets/images/shop/seed.png"
import SeedClick from "../../assets/images/shop/seedclick.png"
import Nong from "../../assets/images/shop/nong.png"
import NongClick from "../../assets/images/shop/nongclick.png"
import Cloth from "../../assets/images/shop/cloth.png"
import ClothClick from "../../assets/images/shop/clothclick.png"
import BiryoList from "./biryoList"
import ToolList from "./toolList"
import SeedList from "./seedList"
import PesticideList from "./pesticideList"
import ClothList from "./clothList"

export default function MenuBar(){
  const [selected,setSelected] = useState('biryo')

  const menuItems = [
    { key: 'biryo', text: '비료', image: Biryo, clickImage:BiryoClick },
    { key: 'equipment', text: '농기구', image: Gigu,clickImage:GiguClick },
    { key: 'seed', text: '씨앗/모종', image: Seed,clickImage:SeedClick },
    { key: 'pesticide', text: '농약', image: Nong,clickImage:NongClick },
    { key: 'cloth',text: '작업복', image: Cloth,clickImage:ClothClick} ,
  ];

  return(
    <>
    <div className="flex justify-between p-3 px-5"
      style={{
        backgroundColor: "rgba(196, 239, 125, 0.5)"
      }}>
      {menuItems.map(({ key, text, image, clickImage }) => (
        <div key={key} className='flex flex-col items-center justify-center' onClick={()=>setSelected(key)}>
          <img  src={selected === key ? clickImage : image} alt={text} />
          <p className='text-md text-center font-bold mt-2'>{text}</p>
        </div>
      ))}
      </div>
      {selected === "biryo" && <BiryoList /> }
      {selected === 'equipment' && <ToolList />}
      {selected === 'seed' && <SeedList />}
      {selected === 'pesticide' && <PesticideList />}
      {selected === 'cloth' && <ClothList />}
    </>
    
  )
}