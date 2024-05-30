import { getDRDCFlow } from "@/services/monitor";
import TokenCard from "@components/@common/card/TokenCard";
import BarChart from "@components/@common/chart/BarChart";
import LineChart from "@components/@common/chart/LineChart";
import { useEffect, useState } from "react";
import Swal from "sweetalert2";
import Web3 from "web3";

const DRDC = () => {
    // const testDate = (currentDate) => {
    //     const test = new Date(currentDate)
    //     test.setDate(test.getDate() + 1)
    //     return test.toISOString().split('T')[0]
    // }
    const currentDate = new Date()
    const formattedDate = currentDate.toISOString().split('T')[0]

    const sevenDaysAgo = (formattedDate) => {
        const sevenDate = new Date(formattedDate)
        sevenDate.setDate(sevenDate.getDate() - 6)
        return sevenDate.toISOString().split('T')[0]
    }

    const [fromDate, setFromDate] = useState(sevenDaysAgo(formattedDate))
    const [toDate, setToDate] = useState(formattedDate)
    
    const [fromDate1, setFromDate1] = useState(sevenDaysAgo(formattedDate))
    const [toDate1, setToDate1] = useState(formattedDate)

    const [noData, setNoData] = useState(0)
    const [warningMessage, setWarningMessage] = useState("");

    const [mintedList, setMintedList] = useState([])
    const [burnedList, setBurnedList] = useState([])

    const changeDate = () => {
        if (toDate > formattedDate) {
            setWarningMessage("조회 종료일은 오늘보다 미래일 수 없습니다.")
            setFromDate(sevenDaysAgo(formattedDate))
            setToDate(formattedDate)
            return;
        } else if (fromDate > toDate) {
            setWarningMessage("조회 시작일은 조회 종료일보다 먼저여야 합니다.");
            setFromDate(sevenDaysAgo(formattedDate))
            setToDate(formattedDate)
            return;
        } else if (fromDate < toDate) {
                // getDRDCFlow(
                //     fromDate,
                //     toDate,
                //     res => {
                //         console.log(res)
                //         console.log(fromDate, toDate)
                //         if (res.mintedList && res.burnedList) {
                //             setNoData(0)
                //             setMintedList(res.mintedList)
                //             setBurnedList(res.burnedList)
                //             console.log(noData)
                //         } else {
                //             setNoData(1)
                //         }
                //     },
                //     err => {
                //         console.log("불러오기 안됩니다.", err)
                //     }
                // )
                console.log(fromDate, toDate)
        }
    }

    useEffect(() => {
        // getDRDCFlow(
        //     fromDate,
        //     toDate,
        //     res => {
        //         setMintedList(res.mintedList)
        //         setBurnedList(res.burnedList)
        //         console.log(res)
        //         console.log(fromDate, toDate)
        //     },
        //     err => {
        //         console.log("불러오기 안됩니다.", err)
        //     }
        // )
    }, [])

    const [totalMinted, setTotalMinted] = useState(null);
    const [totalBurned, setTotalBurned] = useState(null);
    const [totalCirculation, setTotalCirculation] = useState(null);

    //web3 인스턴스생성 - 가나슈(블록체인 서버) 주소
    const web3 = new Web3('http://k10s209.p.ssafy.io:8545/')

    //계약주소 - yml 파일(혹은 [노션]-[블록체인 지식])에 있음
    const contractAddress = '0x6e7E897293BD779228ce3920040da9d776b88224'

    // 블록체인 컨트랙트 안에 있는 함수나 변수 정의
    const contractABI = [
        {
            "inputs": [
                {
                    "internalType": "address",
                    "name": "owner",
                    "type": "address"
                },
                {
                    "internalType": "address",
                    "name": "spender",
                    "type": "address"
                }
            ],
            "name": "allowance",
            "outputs": [
                {
                    "internalType": "uint256",
                    "name": "",
                    "type": "uint256"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [
                {
                    "internalType": "address",
                    "name": "account",
                    "type": "address"
                }
            ],
            "name": "balanceOf",
            "outputs": [
                {
                    "internalType": "uint256",
                    "name": "",
                    "type": "uint256"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "decimals",
            "outputs": [
                {
                    "internalType": "uint8",
                    "name": "",
                    "type": "uint8"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "name",
            "outputs": [
                {
                    "internalType": "string",
                    "name": "",
                    "type": "string"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "owner",
            "outputs": [
                {
                    "internalType": "address",
                    "name": "",
                    "type": "address"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "symbol",
            "outputs": [
                {
                    "internalType": "string",
                    "name": "",
                    "type": "string"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "totalBurned",
            "outputs": [
                {
                    "internalType": "uint256",
                    "name": "",
                    "type": "uint256"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "totalMinted",
            "outputs": [
                {
                    "internalType": "uint256",
                    "name": "",
                    "type": "uint256"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [],
            "name": "totalSupply",
            "outputs": [
                {
                    "internalType": "uint256",
                    "name": "",
                    "type": "uint256"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        },
        {
            "inputs": [
                {
                    "internalType": "address",
                    "name": "",
                    "type": "address"
                }
            ],
            "name": "trustedContracts",
            "outputs": [
                {
                    "internalType": "bool",
                    "name": "",
                    "type": "bool"
                }
            ],
            "stateMutability": "view",
            "type": "function"
        }
    ]    

    //계약 인스턴스생성
    const contract = new web3.eth.Contract(contractABI, contractAddress)

    //총발행량 불러오기
    useEffect(() => {
        contract.methods.totalMinted().call()
            .then((result) => {
                console.log("총발급량", result);
                setTotalMinted(result);
            })
            .catch((error) => {
                console.error(error);
            })
    }, []);

    //총소각량 불러오기
    useEffect(() => {
        contract.methods.totalBurned().call()
            .then((result) => {
                console.log("소각량", result);
                setTotalBurned(result);
            })
            .catch((error) => {
                console.error(error);
            })
    }, [])

    //총유통량 정하기
    useEffect(() => {
        if (totalMinted !== null && totalBurned !== null) {
            setTotalCirculation(totalMinted - totalBurned)
        }
    }, [totalMinted, totalBurned])

    //총유통량 로그찍기용
    useEffect(() => {
        if (totalCirculation !== null) {
            console.log("총 유통량", totalCirculation)
        }
    }, [totalCirculation])

    useEffect(() => {

    }, [])

    return (
        <div className="flex justify-center a w-full h-full bg-CONTENT">
            <div className=" p-4 w-5/6 h-80 bg-DETAIL mt-5">
                {/* 유통량, 제조량, 폐기량 */}
                <div className="w-full h-24 flex ">
                    {totalCirculation != null && <TokenCard title="DRDC 유통량" amount={totalCirculation} />}
                    {totalMinted != null && <TokenCard title="DRDC 발급량" amount={totalMinted} />}
                    {totalBurned != null && <TokenCard title="DRDC 소각량" amount={totalBurned} />}
                </div>
                <div className=" mt-2 h-40 w-full h-10 bg-white p-2">
                    <div className="flex items-center">
                        <p className="text-left text-xs font-bold mr-2">최근 유동 내역</p>
                        <div className="flex items-center">
                            <label className={`input input-bordered mr-2 flex items-center h-6`}>
                                <input
                                    type="date"
                                    id="start"
                                    name="start"
                                    placeholder="조회시작일"
                                    value={fromDate ? fromDate : ''}
                                    onChange={(event) => {setFromDate(event.target.value)}}
                                    onFocus={() => setWarningMessage("")}
                                />
                            </label>
                            ~
                            <label className={`input input-bordered ml-2 flex items-center  h-6`}>
                                <input
                                    type="date"
                                    id="end"
                                    name="end"
                                    placeholder="조회마지막일"
                                    value={toDate ? toDate : ''}
                                    onChange={(event) => {setToDate(event.target.value)}}
                                    onFocus={() => setWarningMessage("")}
                                />
                            </label>
                            <button style={{ minHeight: 'auto' }} className="btn btn-neutral ml-2 h-6" onClick={() => changeDate()}>검색</button>
                            <div className="ml-4 text-red-500 font-bold text-sm">{warningMessage}</div>
                        </div>
                    </div>
                    {noData == 1 && <div className="font-bold flex justify-center items-center h-full">불러올 수 있는 데이터가 없습니다.</div>}
                    {/* {noData == 0 && fromDate && <BarChart flowData={fromDate} maxValue={maxValue}></BarChart>} */}
                </div>
                <div className=" mt-2 h-30 w-full h-10 bg-white p-3">
                    <div className="flex items-center">
                        <p className="text-left text-xs font-bold mr-2">최근 유동 내역</p>
                        <div className="items-center">
                            <label className={`input input-bordered mr-2`}>
                                <input
                                    type="date"
                                    id="start"
                                    name="start"
                                    placeholder="조회시작일"
                                    value={fromDate1 ? fromDate1 : ''}
                                    onChange={(event) => {setFromDate1(event.target.value)}}
                                />
                            </label>
                            ~
                            <label className={`input input-bordered ml-2`}>
                                <input
                                    type="date"
                                    id="end"
                                    name="end"
                                    placeholder="조회마지막일"
                                    value={toDate1 ? toDate1 : ''}
                                    onChange={(event) => {setToDate1(event.target.value)}}
                                />
                            </label>
                            <button style={{ minHeight: 'auto' }} className="btn btn-neutral ml-2 h-6">검색</button>
                        </div>
                    </div>
                    {/* <LineChart /> */}
                </div>
            </div>
        </div>
    )
}

export default DRDC; 