import Header from "@components/@common/Header/Header";
import SideBar from "@components/@common/SideBar/SideBar";
import ReviewContent from "@components/review/ReviewContent";

const ReviewPage = () => {
    return (
        <div className="flex w-screen h-screen">
            <SideBar/>
            <div className="flex-1 h-full overflow-hidden bg-slate-500">
                <Header label={"심 사"} />
                <ReviewContent />
            </div>
        </div>
    );
};

export default ReviewPage;