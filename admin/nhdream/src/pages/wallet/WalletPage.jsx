import Header from "@components/@common/Header/Header";
import SideBar from "@components/@common/SideBar/SideBar";
import WalletContent from "@components/wallet/WalletContent";

const WalletPage = () => {
    return (
        <div className="flex w-screen h-screen">
            <SideBar/>
            <div className="flex-1 h-full overflow-hidden bg-slate-500">
                <Header label={"지 갑 관 리"}/>
                <WalletContent/>
            </div>
        </div>
    );
};

export default WalletPage;