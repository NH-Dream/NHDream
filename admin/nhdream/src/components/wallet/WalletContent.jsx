import Navigator from "@components/@common/Navigator/Navigator"
import { Outlet } from "react-router-dom";

const WalletContent = () => {

    // 네비게이션 라벨 & 링크
    const links = [
        {
            label: "당행 전자지갑관리", 
            link: "/wallet"
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

export default WalletContent; 