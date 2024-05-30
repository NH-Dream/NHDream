import User from '@/assets/images/review/user.png'
import Buisiness from '@/assets/images/review/buisiness.png'
import Attachment from '@/assets/images/review/attachment.png'
import DetailContent from './detail/DetailContent'
import { useState, useEffect } from 'react'
import DetailImg from './detail/DetailImg'
import {getFarmerReviewDetail} from "@/services/review"

const BuisinessModal = ({title, isOpen, setIsOpen, selectedFarmerId, selectReviewStatus }) => {

    // 모달 오픈 여부    
    const toggleModal = () => {
        setIsOpen(!isOpen);
    };

    useEffect(() => {    
        if(isOpen){
            getFarmerReviewDetail(
                selectedFarmerId, 
                response => {
                    resetUserInfos(response);
                    console.log("목록 불러오기 성공", response)
                },
                error => {
                    console.log("목록 불러오기 실패", error)
                },
            )


        }
    }, [isOpen])


    // 데이터
    const [userInfos, setUserInfos] = useState([
        { label: "성명", content: "김싸피"},
        { label: "이메일", content: "bonheur9813@naver.com" },
        { label: "전화번호", content: "01012341234" },
        { label: "생년월일(YYMMDD)", content: "980914"},
        { label: "직업", content: "교육수료자" },
        { label: "NH토큰 보유량", content: "2,000,000" },
    ])

    const [buisinessInfos, setBuisinessInfos] = useState([
        { label: "대표자", content: "김싸피" },
        { label: "개업일자", content: "20140112" },
        { label: "사업자등록번호", content: "0123456789" },
    ])

    const [certImgs, setCerImgs] = useState([
        { label: "사업자등록증", content: "김싸피"},
    ])

    const resetUserInfos  = (newData) => {
        setUserInfos([
            { label: "성명", content: newData.userName },
            { label: "이메일", content: newData.email },
            { label: "전화번호", content: newData.phone },
            { label: "생년월일(YYMMDD)", content: newData.birthDate },
        ]);

        setBuisinessInfos([
            { label: "대표자", content:newData.representative},
            { label: "개업일자", content: newData.openDate},
            { label: "사업자등록번호", content: newData.licenseNum},
        ]);
        setCerImgs([
            { label: "사업자등록증", content: newData.businessLicense},
        ])
    };



    return (
        <div>
            {/* 모달 */}
            {isOpen && (
                <div id="default-modal" tabIndex="-1" aria-hidden="true" className="fixed top-0 right-0 left-0 z-50 flex justify-center items-center w-full h-full bg-black bg-opacity-50 ">
                    <div className="relative p-4 w-full max-w-2xl max-h-full">
                        {/* 모달 콘텐츠 */}
                        <div className="relative bg-white rounded-lg shadow dark:bg-gray-700 max-h-[700px] overflow-hidden">
                            {/* 모달 헤더 */}
                            <div className="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600 h-[70px]">
                                <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                                    {title}
                                </h3>
                                <button type="button" onClick={toggleModal} className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white" data-modal-hide="default-modal">
                                    <svg className="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                                        <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                                    </svg>
                                    <span className="sr-only">Close modal</span>
                                </button>
                            </div>
                            {/* 모달 바디 */}
                            <div className="h-[560px] p-4 md:p-5 space-y-4 overflow-y-scroll">
                                <div>
                                    <div className='flex mb-2'>
                                        <img style={{width:'22px', height:'22px'}} src={User} /> 
                                        <div className="font-bold text-lg ml-1">신청자정보</div>
                                    </div>
                                    <div className='border-2 p-2 rounded-xl pb-3'>
                                        {userInfos.map((userInfo, index) => (
                                            <DetailContent 
                                                label={userInfo.label}
                                                content={userInfo.content}
                                            />
                                        ))}
                                    </div>

                                    <div className='flex my-2'>
                                        <img style={{width:'25px', height:'25px'}} src={Buisiness} />
                                        <div className="font-bold text-lg ml-1">사업자정보</div>
                                    </div>
                                    <div className='border-2 p-2 rounded-xl pb-3'>
                                        {buisinessInfos.map((buisinessInfo, index) => (
                                            <DetailContent 
                                                label={buisinessInfo.label}
                                                content={buisinessInfo.content}
                                            />
                                        ))}
                                    </div>

                                    <div className='flex my-2'>
                                        <img style={{width:'25px', height:'25px'}} src={Attachment} />
                                        <div className="font-bold text-lg ml-1">첨부파일</div>               
                                    </div> 
                                    <div className='border-2 p-2 rounded-xl pb-3'>
                                    {certImgs.map((certImg, index) => (
                                            <DetailImg 
                                                label={certImg.label}
                                                imgAddress={certImg.content}
                                            />
                                        ))}
                                    </div>
                                </div>
                                
                            
                            </div>
                            {/* 모달 푸터 */}
                            { selectReviewStatus == "PENDING" &&
                                <div className="h-[70px] flex items-center p-4 md:p-5 border-t justify-around bg-[#F3F3F3]"> 
                                <button type="button" 
                                    className="flex justify-around bg-red-200 text-white font-bold py-2.5 px-5 rounded-lg hover:bg-red-300 w-32"
                                    onClick={() => sendResult(0)}
                                >
                                    <img style={{width:'25px', height:'25px'}} src={Reject} />
                                    <div>
                                        거 부
                                    </div>
                                </button>
                                <button type="button" 
                                    className="flex justify-around bg-APPROVAL text-white font-bold py-2.5 px-5 rounded-lg hover:bg-green-300 w-32"
                                    onClick={() => sendResult(1)}
                                >
                                    <img style={{width:'25px', height:'25px'}} src={Approval} />
                                    <div>
                                        승 인
                                    </div>
                                </button>
                            </div>
                            }
                            { selectReviewStatus == "APPROVED" &&
                                <div className="h-[70px] flex items-center p-4 md:p-5 border-t justify-around bg-APPROVAL"> 
                                    <div className='text-[#1E7572] text-2xl font-black'>승 인 완 료</div>
                                </div>
                            }
                            { selectReviewStatus == "REJECTED" &&
                                <div className="h-[70px] flex items-center p-4 md:p-5 border-t justify-around bg-REJECT">
                                    <div className='text-[#FF0000] text-2xl font-black'>승 인 반 려</div>
                                </div>
                            }
                        </div>
                    </div>
                </div>
            )}
        </div>
    );

}

export default BuisinessModal;