import React, { useState, useEffect } from "react";
import {getFarmerReviewList} from "@/services/review"; 
import FarmerData from "./FarmerData";
import BuisinessModal from "@components/@common/modal/BuisinessModal"

const Farmer = () => {
    const [isOpen, setIsOpen] = useState(false);                // 상세 정보 조회 모달
    const [selectedFarmerId, setSelectedFarmerId] = useState(0);    // 선택된 농부 인증 PK(상세정보 API)
    const [selectReviewStatus, setSelectReviewStatus] = useState(""); 


    const [searchName, setSearchName] = useState("");           // 신청자명
    const [farmerReviews, setFarmerReviews] = useState([]);     // 데이터 목록
    const [totalPage, setTotalPage] = useState(0);              // 전체 페이지 수
    const [activeIndex, setActiveIndex] = useState(0);          // 현재 페이지 인덱스


    // 모달 오픈 
    const changeModalStatus = (farmerReviewId, farmerReviewStatus) =>{
        setSelectedFarmerId(farmerReviewId)
        setSelectReviewStatus(farmerReviewStatus)
        setIsOpen(true)
    }

    // 신청자명 엔터를 눌렀을 떄 발생하는 이벤트
    const activeEnter = (e) => {
        if(e.key === "Enter") {
            changeUsername()
        }
    }

    // 첫 렌더링시 API 조회
    useEffect(() =>{
        getFarmerReviewList(
            activeIndex,
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setFarmerReviews(response.farmerReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [])

    // 페이지 변경시 API 조회 
    useEffect(() => {
        getFarmerReviewList(
            activeIndex,
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setFarmerReviews(response.farmerReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [activeIndex])

    // 이름 검색시 API 조회
    const changeUsername = () =>{
        getFarmerReviewList(
            0,
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setFarmerReviews(response.farmerReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }
    

    return(
        <div className="flex flex-col items-center w-full h-full mt-3">
            <div className="flex w-5/6 h-12 ">
                <label style={{ maxWidth: "15rem" }} className="flex items-center w-full gap-2 input input-bordered input-sm">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor" className="w-4 h-4 opacity-50"><path fillRule="evenodd" d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z" clipRule="evenodd" /></svg>
                    <input 
                        type="text" 
                        className="grow" 
                        placeholder="신청자명을 검색해주세요." 
                        value={searchName}
                        onChange={(event) => setSearchName(event.target.value)}
                        onKeyDown={(event) => activeEnter(event)}
                        onBlur={() => changeUsername()}
                    />
                </label>
            </div>
            
            <div className="w-5/6 overflow-x-auto bg-white h-4/6">    
                <div>
                    <table className="table text-center">
                        <thead>
                            <tr>
                                <th>NO</th> 
                                <th>신청자명</th>
                                <th>사업자등록번호</th>
                                <th>신청일자</th>
                                <th>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            {farmerReviews.map((farmerReview, index) => (
                                <FarmerData 
                                    key = {index}
                                    index = {index}
                                    activeIndex = {activeIndex*10}
                                    data = {farmerReview}
                                    changeModalStatus = {changeModalStatus}
                                />
                            ))}
                        </tbody>
                    </table>
                </div>

                <BuisinessModal title={"귀농인증"} isOpen={isOpen} setIsOpen={setIsOpen} selectedFarmerId={selectedFarmerId} selectReviewStatus={selectReviewStatus} setSelectReviewStatus={setSelectReviewStatus} />
            </div>  

            <div className="mt-5 join">
                {Array.from({ length: totalPage }, (_, index) => (
                    <button className={`bg-white join-item btn btn-sm ${activeIndex === index ? 'bg-green-500' : ''}`} onClick={() => setActiveIndex(index)}>{index+1}</button>
                ))}
            </div>
        </div>
    )
}

export default Farmer; 