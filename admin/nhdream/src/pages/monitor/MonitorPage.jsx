import Header from "@components/@common/Header/Header";
import SideBar from "@components/@common/SideBar/SideBar";

const MonitorPage = () => {
    return (
        <div className="flex w-screen h-screen">
            <SideBar/>
            <div className="flex-1 h-full bg-slate-500">
                <Header></Header>
            </div>
        </div>
    );
};

export default MonitorPage;