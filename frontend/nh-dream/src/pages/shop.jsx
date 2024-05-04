import TopBar from "../components/shop/topBar"
import MenuBar from "../components/shop/menuBar"
import ItemList from "../components/shop/itemList"

export default function ShopPage(){
  return(
    <div>
      <TopBar />
      <MenuBar />
      <ItemList />
    </div>
  )
}