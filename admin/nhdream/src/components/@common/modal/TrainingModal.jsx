import User from '@/assets/images/review/user.png'
import EDUCATION from '@/assets/images/review/education.png'
import Attachment from '@/assets/images/review/attachment.png'
import Approval from '@/assets/images/review/approval.png'
import Reject from '@/assets/images/review/reject.png'
import DetailContent from './detail/DetailContent'
import { useState, useEffect } from 'react'
import DetailImg from './detail/DetailImg'
import {getTrainingReviewDetail, sendTrainingReviewResult} from "@/services/review"
import Swal from "sweetalert2"

const TrainingModal = ({title, isOpen, setIsOpen, selectedTrainingId, selectReviewStatus, setSelectReviewStatus }) => {

    const [userId, setUserId] = useState(0);

    // 모달 오픈 여부    
    const toggleModal = () => {
        setIsOpen(!isOpen);
    };

    useEffect(() => {    
        if(isOpen){
            console.log(selectedTrainingId)
            getTrainingReviewDetail(
                selectedTrainingId, 
                response => {
                    resetUserInfos(response );
                    setUserId(response.userId)
                    console.log("목록 불러오기 성공", response)
                },
                error => {
                    console.log("목록 불러오기 실패", error)
                },
            )


        }
    }, [isOpen])

    const sendResult = (reviewResult) => {
        sendResultApi(reviewResult)
    } 

    const sendResultApi = (reviewResult) => {
        sendTrainingReviewResult(
            {
                userId: userId, 
                applyPostId: selectedTrainingId,
                result: reviewResult,
            }, 
            response => {
                console.log("교육 심사 성공", response)
                if(reviewResult == 1){
                    setSelectReviewStatus("APPROVED")
                }else{
                    setSelectReviewStatus("REJECTED")
                }
            },
            error => {
                console.log("교육 심사 실패", error)
                Swal.fire({
                    icon: "warning",
                    html: `
                    <b>교육 심사 실패</b>
                    `,
                    showConfirmButton: false 
                });
            },
        )
    }
    


    // 데이터
    const [userInfos, setUserInfos] = useState([
        { label: "성명", content: "김싸피"},
        { label: "이메일", content: "bonheur9813@naver.com" },
        { label: "전화번호", content: "01012341234" },
        { label: "생년월일(YYMMDD)", content: "980914"},
        { label: "직업", content: "교육수료자" },
        { label: "NH토큰 보유량", content: "2,000,000" },
    ])

    const [trainingInfos, setTrainingInfos] = useState([
        { label: "교육명", content: "청년농부사관학교" },
        { label: "교육기관", content: "NH농협" },
        { label: "기수", content: "10" },
    ])

    const [certImgs, setCerImgs] = useState([
        { label: "수료증", content: "김싸피"},
    ])

    const resetUserInfos  = (newData) => {
        setUserInfos([
            { label: "성명", content: newData.userName },
            { label: "이메일", content: newData.email },
            { label: "전화번호", content: newData.phone },
            { label: "생년월일(YYMMDD)", content: newData.birthDate },
        ]);

        setTrainingInfos([
            { label: "교육명", content:newData.trainingName},
            { label: "교육기관", content: newData.trainingInstitution},
            { label: "기수", content: newData.ordinal},
        ]);
        setCerImgs([
            { label: "수료증", content: newData.certificate},
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
                                        <img style={{width:'25px', height:'25px'}} src={EDUCATION} />
                                        <div className="font-bold text-lg ml-1">교육정보</div>
                                    </div>
                                    <div className='border-2 p-2 rounded-xl pb-3'>
                                        {trainingInfos.map((trainingInfo, index) => (
                                            <DetailContent 
                                                label={trainingInfo.label}
                                                content={trainingInfo.content}
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

export default TrainingModal;