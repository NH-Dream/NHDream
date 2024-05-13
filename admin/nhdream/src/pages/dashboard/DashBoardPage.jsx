import Header from "@components/@common/Header/Header";
import SideBar from "@components/@common/SideBar/SideBar";
import ReviewContent from "@components/review/ReviewContent";


const DashBoardPage = () => {
    return (
        <div className="flex w-screen h-screen">
            <SideBar/>
            <div className="flex-1 h-full bg-CONTENT">
                <Header label={"DashBoard"} />
                <ReviewContent />
            </div>
        </div>
    );
};

export default DashBoardPage;