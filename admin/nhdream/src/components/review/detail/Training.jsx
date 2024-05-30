import React, { useState, useEffect } from "react";
import {getTrainingReviewList} from "@/services/review"; 
import TrainingData from "@components/review/detail/TrainingData"
import TrainingModal from "@components/@common/modal/TrainingModal"

const Training = () => {
    const [isOpen, setIsOpen] = useState(false);                        // 상세 모달 개폐 여부
    const [selectedTrainingId, setSelectedTrainingId] = useState(0);    // 상세 선택된 교육 PK
    const [selectReviewStatus, setSelectReviewStatus] = useState("");   // 선택된 심사 상태
    const [dropStatus, setDropStatus] = useState("ALL");                // 심사상태 드롭다운 값
    const [dropdownOpen, setDropdownOpen] = useState(false);            // 상태 드롭다운 개폐여부
    const [searchName, setSearchName] = useState("");                   // 신청자명
    const [trainingReviews, setTrainingReviews] = useState([]);         // 데이터 목록
    const [totalPage, setTotalPage] = useState(0);                      // 전체 페이지 수
    const [activeIndex, setActiveIndex] = useState(0);                  // 현재 페이지 인덱스

    // 모달 오픈 
    const changeModalStatus = (selectedTrainingId, trainingReviewStatus) =>{
        setSelectedTrainingId(selectedTrainingId)
        setSelectReviewStatus(trainingReviewStatus)
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
        getTrainingReviewList(
            0,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setTrainingReviews(response.trainingReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [])
    
    // 심사 상태 변경시 API 조회
    useEffect(() => {
        getTrainingReviewList(
            0,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setTrainingReviews(response.trainingReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [dropStatus])

    // 페이지 변경시 API 조회 
    useEffect(() => {
        getTrainingReviewList(
            activeIndex,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setTrainingReviews(response.trainingReviewContentDtoList)
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [activeIndex])

    // 이름 검색시 API 조회
    const changeUsername = () =>{
        getTrainingReviewList(
            0,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setTrainingReviews(response.trainingReviewContentDtoList)
                setDropStatus("ALL");
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }

    useEffect(() =>{
        getTrainingReviewList(
            activeIndex,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage)
                setTrainingReviews(response.trainingReviewContentDtoList)
                setDropStatus("ALL");
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [selectReviewStatus])

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
                <div className="relative ml-2">
                    <button
                        id="dropdownHoverButton"
                        onMouseEnter={() => setDropdownOpen(true)}
                        onMouseLeave={() => setDropdownOpen(false)}
                        
                        style={{  height: "2rem" }}
                        className="w-40 bg-white border rounded-lg text-sm  px-3 py-2.5 text-center inline-flex items-center flex justify-between input-bordered"
                        type="button"
                    >
                        <span>상태 : </span>
                        <span className="flex-grow text-left ml-1">{dropStatus}</span>
                        <svg className="w-2.5 h-2.5 ms-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6">
                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 1 4 4 4-4" />
                        </svg>
                    </button>
                    {dropdownOpen && (
                        <div
                            id="dropdownHover"
                            className="absolute z-10 bg-white divide-y divide-gray-100 rounded-lg shadow w-44 dark:bg-gray-700 "
                            onMouseEnter={() => setDropdownOpen(true)}
                            onMouseLeave={() => setDropdownOpen(false)}
                        >
                            <ul className="py-2 text-sm text-gray-700 dark:text-gray-200" aria-labelledby="dropdownHoverButton">
                            <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => setDropStatus("ALL")}
                                    >
                                        ALL
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => setDropStatus("PENDING")}
                                    >
                                        PENDING
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => setDropStatus("APPROVED")}
                                    >
                                        APPROVED
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => setDropStatus("REJECTED")}
                                    >
                                        REJECTED
                                    </a>
                                </li>
                            </ul>
                        </div>
                    )}
                </div>

            </div>
            
            <div className="w-5/6 overflow-x-auto bg-white h-4/6">    
                <div>
                    <table className="table text-center">
                        <thead>
                            <tr>
                                <th>NO</th> 
                                <th>신청자명</th>
                                <th>교육명</th>
                                <th>신청일자</th>
                                <th>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            {trainingReviews.map((trainingReview, index) => (
                                <TrainingData 
                                    key = {index}
                                    index = {index}
                                    activeIndex = {activeIndex*10}
                                    data = {trainingReview}
                                    changeModalStatus = {changeModalStatus}
                                />
                            ))}
                        </tbody>
                    </table>
                </div>

                <TrainingModal title={"교육심사"} isOpen={isOpen} setIsOpen={setIsOpen} selectedTrainingId={selectedTrainingId} selectReviewStatus={selectReviewStatus} setSelectReviewStatus={setSelectReviewStatus} />
            </div>  

            <div className="mt-5 join">
                {Array.from({ length: totalPage }, (_, index) => (
                    <button className={`bg-white join-item btn btn-sm ${activeIndex === index ? 'bg-green-500' : ''}`} onClick={() => setActiveIndex(index)}>{index+1}</button>
                ))}
            </div>
        </div>
    )
}

export default Training; 