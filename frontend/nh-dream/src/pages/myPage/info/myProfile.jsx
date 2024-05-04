import { MyInfoComponent } from "../../../components/mypage/myProfile/index";
import TopBar from "../../../components/common/topBar";

export default function MyInfoPage() {
  return (
    <div>
      <TopBar title="내 정보" color="#ffffff"/>
      <MyInfoComponent />
    </div>
  )
}