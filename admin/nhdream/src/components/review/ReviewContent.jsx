import Navigator from "@components/@common/Navigator/Navigator"
import { Outlet } from "react-router-dom";

const ReviewContent = () => {

    // 네비게이션 라벨 & 링크
    const links = [
        {
            label: "대 출", 
            link: "/"
        }, 
        {
            label: "교 육 인 증", 
            link: "/training"
        }, 
        {
            label: "귀 농 인 증", 
            link: "/farmer"
        }
    ]

    return(
        <div className="h-full bg-CONTENT">
            <div className="flex justify-center w-full">
                <Navigator links={links} />
            </div>
            
            <Outlet/>
        </div>
    )
}

export default ReviewContent; 