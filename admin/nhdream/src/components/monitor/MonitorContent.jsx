import Navigator from "@components/@common/Navigator/Navigator"
import { Outlet } from "react-router-dom";

const MonitorContent = () => {

    // 네비게이션 라벨 & 링크
    const links = [
        {
            label: "NHDC", 
            link: "/monitoring"
        }, 
        {
            label: "DRDC", 
            link: "/monitoring/drdc"
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

export default MonitorContent; 