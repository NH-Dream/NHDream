import Header from "@components/@common/Header/Header";
import SideBar from "@components/@common/SideBar/SideBar";
import MonitorContent from "@components/monitor/MonitorContent";

const MonitorPage = () => {
    return (
        <div className="flex w-screen h-screen">
            <SideBar/>
            <div className="flex-1 h-full overflow-hidden bg-slate-500">
                <Header label={"통 계 현 황"} />
                <MonitorContent />
            </div>
        </div>
    );
};

export default MonitorPage;