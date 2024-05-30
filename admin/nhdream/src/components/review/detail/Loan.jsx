import LoanModal from "@components/@common/modal/LoanModal";
import React, { useState, useEffect } from "react";
import {getLoanReviewList} from "@/services/review"
import LoanData from "./LoanData";


const Loan = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [loanReviews, setLoanReviews] = useState([]);
    const [selectedLoanId, setSelectedLoanId] = useState(0);
    const [selectReviewStatus, setSelectReviewStatus] = useState(""); 
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [dropStatus, setDropStatus] = useState("ALL"); 
    const [searchName, setSearchName] = useState(""); 
    


    // 모달 오픈 
    const changeModalStatus = (loanReviewId, loanReviewStatus) =>{
        setSelectedLoanId(loanReviewId)
        setSelectReviewStatus(loanReviewStatus)
        setIsOpen(true)
    }



    // 목록 조회 API 연결
    useEffect(() =>{
        getLoanReviewList(
            0,
            dropStatus, 
            searchName,
            response => {
                console.log("목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [])

    useEffect(() =>{
        getLoanReviewList(
            0,
            dropStatus,
            searchName,
            response => {
                console.log("selectReviewStatus 목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [selectReviewStatus])

    useEffect(() =>{
        console.log("이름 바뀜!")
        getLoanReviewList(
            0,
            dropStatus,
            searchName,
            response => {
                console.log("selectReviewStatus 목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    }, [dropStatus])

    // 페이지네이션 인덱스 추적을 위한 변수
    const [totalPage, setTotalPage] = useState(0);
    const [activeIndex, setActiveIndex] = useState(0);
    
    // 버튼 클릭 핸들러
    const handleClick = (index) => {
        // 클릭된 버튼의 인덱스를 상태에 저장
        console.log("버튼눌림", index)
        setActiveIndex(index);
        getLoanReviewList(
            index,
            dropStatus,
            searchName, 
            response => {
                console.log("페이지네이션 목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    };

    // 상태 변화
    const changeReviewStatus = (change) => {
        // 클릭된 버튼의 인덱스를 상태에 저장
        setDropStatus(change);
        getLoanReviewList(
            0,
            change,
            searchName,
            response => {
                console.log("하이루 목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
            },
            error => {
                console.log("목록 불러오기 실패", error)
            },
        )
    };

    const changeUsername = () => {
        getLoanReviewList(
            0,
            dropStatus,
            searchName,
            response => {
                console.log("이름 목록 불러오기 성공", response)
                setTotalPage(response.totalPage);
                setLoanReviews(response.loanReviewContentDtoList); 
                setDropStatus("ALL");
            },
            error => {
                console.log("이름 불러오기 실패", error)
            },
        )
    };

    // 신청자명 엔터를 눌렀을 떄 발생하는 이벤트
    const activeEnter = (e) => {
        if(e.key === "Enter") {
            changeUsername()
        }
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
                                        onClick={() => changeReviewStatus("ALL")}
                                    >
                                        ALL
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => changeReviewStatus("PENDING")}
                                    >
                                        PENDING
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => changeReviewStatus("APPROVED")}
                                    >
                                        APPROVED
                                    </a>
                                </li>
                                <li>
                                    <a href="#" 
                                        className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                                        onClick={() => changeReviewStatus("REJECTED")}
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
                                <th>상품명</th>
                                <th>신청자명</th>
                                <th>신청대출액</th>
                                <th>이자율</th>
                                <th>신청일자</th>
                                <th>상태</th>
                            </tr>
                        </thead>
                        <tbody>
                            {loanReviews.map((loanReview, index) => (
                                <LoanData 
                                    key = {index}
                                    index = {index}
                                    activeIndex = {activeIndex*10}
                                    data = {loanReview}
                                    changeModalStatus = {changeModalStatus}
                                />
                            ))}
                        </tbody>
                    </table>
                </div>

                <LoanModal title={"대출심사"} isOpen={isOpen} setIsOpen={setIsOpen} selectedLoanId={selectedLoanId} selectReviewStatus={selectReviewStatus} setSelectReviewStatus={setSelectReviewStatus} />
            </div>  

            <div className="mt-5 join">
                {Array.from({ length: totalPage }, (_, index) => (
                    <button className={`bg-white join-item btn btn-sm ${activeIndex === index ? 'bg-green-500' : ''}`} onClick={() => handleClick(index)}>{index+1}</button>
                ))}
            </div>
        </div>
    )
}

export default Loan; 