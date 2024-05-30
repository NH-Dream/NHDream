import TokenCard from "@components/@common/card/TokenCard";
import BarChart from "@components/@common/chart/BarChart";
import LineChart from "@components/@common/chart/LineChart";
import { useEffect, useState } from "react";
import Web3 from "web3";
import {getNHDCFlow, getNHDCTradeFlow} from "@/services/monitor"



const NHDC = () => {

    //web3 인스턴스생성
    const web3 = new Web3('http://k10s209.p.ssafy.io:8545/');
    //Get함수만 따로 뺀 ABI
    const contractABI =[
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
            "name": "getInterestWalletBalance",
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
            "name": "interestWallet",
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
            "name": "loanContract",
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
            "name": "totalInterestMinted",
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
            "name": "totalInterestPaid",
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
        }
    ]
    //계약주소
    const contractAddress = '0x1D0C65C898334649e960031577B4D0FcD5661Aa7'
    //계약 인스턴스생성
    const contract = new web3.eth.Contract(contractABI,contractAddress)

    const [totalMinted, setTotalMinted] = useState(null);
    const [totalBurned, setTotalBurned] = useState(null);
    const [totalCirculation, setTotalCirculation] = useState(null);
    //총발행량 불러오기
    useEffect(() => {
        contract.methods.totalMinted().call()
        .then((result) => {
            console.log("총발급량",result);
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
            console.log("소각량",result);
            setTotalBurned(result);
        })
        .catch((error) => {
            console.error(error);
        })
    }, [])

    //총유통량 정하기
    useEffect(() => {
        if(totalMinted !== null && totalBurned !== null) {
            setTotalCirculation(totalMinted-totalBurned)
        }
    }, [totalMinted,totalBurned])
    //총유통량 로그찍기용
    useEffect(() => {
        if(totalCirculation !== null){
            console.log("총 유통량", totalCirculation)
        }
    }, [totalCirculation])

    // 유동량 불러오기
    const [flowStart, setFlowStart] = useState(new Date(new Date().setDate(new Date().getDate() - 6)).toISOString().slice(0, 10));
    const [flowEnd, setflowEnd] = useState(new Date().toISOString().slice(0, 10));
    const [tradeStart, setTradeStart] = useState(new Date(new Date().setDate(new Date().getDate() - 6)).toISOString().slice(0, 10));
    const [tradeEnd, setTradeEnd] = useState(new Date().toISOString().slice(0, 10));

    const [flowData, setFlowData] = useState(null);
    const [maxValue, setMaxValue] = useState(7000000);
    const [warningMessage, setWarningMessage] = useState("");

    const [tradeData, setTradeData] = useState(null);
    const [tradeMaxValue, setTradeMaxValue] = useState(7000000);
    const [warningTradeMessage, setWarningTradeMessage] = useState("");

    // Default 유동량 조회
    useEffect(() => {
        if (flowStart && flowEnd) {
            getNHDCFlow(
                flowStart,
                flowEnd,
                response => {
                    setFlowData(response)
                    const max = Math.max(...response.map(item => item.mintAmount), ...response.map(item => item.burnAmount));
                    const roundedMaxValue = Math.ceil(max / 100000) * 100000;
                    setMaxValue(roundedMaxValue);
                    console.log("유동량 불러오기 성공", response);
                },
                error => {
                    console.log("유동량 불러오기 실패", error);
                }
            );
        }
    }, []);

    // Default 거래량 조회
    useEffect(() => {
        if (tradeStart && tradeEnd) {
            getNHDCTradeFlow(
                tradeStart,
                tradeEnd,
                response => {
                    setTradeData(response)
                    const max = Math.max(...response.map(item => item.tradeAmount));
                    const buffer = max * 0.1; // 최대값의 10%를 버퍼로 추가
                    const roundedMaxValue = Math.ceil((max + buffer) / 100000) * 100000;
                    setTradeMaxValue(roundedMaxValue);
                    console.log("거래량 불러오기 성공", response);
                },
                error => {
                    console.log("거래량 불러오기 실패", error);
                }
            );
        }
    }, []);

    const getNHDCFlowData = () => {
        if (flowStart > flowEnd) {
            setWarningMessage("조회 시작일은 조회 종료일보다 먼저여야 합니다.");
            return;
        }
        
        if (flowStart && flowEnd) {
            getNHDCFlow(
                flowStart,
                flowEnd,
                response => {
                    setFlowData(response)
                    const max = Math.max(...response.map(item => item.mintAmount), ...response.map(item => item.burnAmount));
                    const roundedMaxValue = Math.ceil(max / 100000) * 100000;
                    setMaxValue(roundedMaxValue);
                    console.log("유동량 불러오기 성공", response);
                },
                error => {
                    console.log("유동량 불러오기 실패", error);
                }
            );
        }
    }

    const getNHDCTradeData = () => {

        console.log(tradeStart)
        console.log(tradeEnd)
        if (tradeStart > tradeEnd) {
            setWarningTradeMessage("조회 시작일은 조회 종료일보다 먼저여야 합니다.dd");
            return;
        }
        
        if (tradeStart && tradeEnd) {
            getNHDCTradeFlow(
                tradeStart,
                tradeEnd,
                response => {
                    setTradeData(response)
                    const max = Math.max(...response.map(item => item.tradeAmount));
                    const buffer = max * 0.1; // 최대값의 10%를 버퍼로 추가
                    const roundedMaxValue = Math.ceil((max + buffer) / 100000) * 100000;
                    setTradeMaxValue(roundedMaxValue);
                    console.log(TradeMaxValue)
                    console.log("거래량 불러오기 성공", response);
                },
                error => {
                    console.log("거래량 불러오기 실패", error);
                }
            );
        }
    }


    return(
        <div className="flex justify-center a w-full h-full bg-CONTENT">
            <div className=" p-4 w-5/6 h-80 bg-DETAIL mt-5">
                {/* 유통량, 제조량, 폐기량 */}
                <div className="w-full h-24 flex ">    
                    {totalCirculation != null && <TokenCard title="NH토큰 유통량" amount={totalCirculation} />}
                    {totalMinted != null && <TokenCard title="NH토큰 발급량" amount={totalMinted} />}
                    {totalBurned != null && <TokenCard title="NH토큰 소각량" amount={totalBurned} />}
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
                                    value={flowStart ? flowStart : ''}
                                    onChange={(event) => {setFlowStart(event.target.value); }}
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
                                    value={flowEnd ? flowEnd : ''}
                                    onChange={(event) => {setflowEnd(event.target.value)}}
                                    onFocus={() => setWarningMessage("")}
                                />
                            </label>
                            <button style={{ minHeight: 'auto' }} className="btn btn-neutral ml-2 h-6 " onClick={() => getNHDCFlowData()}>검색</button>
                            <div className="ml-4 text-red-500 font-bold text-sm">{warningMessage}</div>
                        </div>
                    </div>
                    {flowData && <BarChart flowData={flowData} maxValue={maxValue}></BarChart>}
                </div>
                <div className=" mt-2 h-30 w-full h-10 bg-white p-3">
                    <div className="flex items-center">
                        <p className="text-left text-xs font-bold mr-2">최근 거래 내역</p>
                        <div className="flex items-center">
                            <label className={`input input-bordered mr-2 flex items-center h-6`}>
                                <input
                                    type="date"
                                    id="start"
                                    name="start"
                                    placeholder="조회시작일"
                                    value={tradeStart ? tradeStart : ''}
                                    onChange={(event) => setTradeStart(event.target.value)}
                                    onFocus={() => setWarningTradeMessage("")}
                                />
                            </label>
                            ~
                            <label className={`input input-bordered mr-2 flex items-center h-6`}>
                                <input
                                    type="date"
                                    id="end"
                                    name="end"
                                    placeholder="조회마지막일"
                                    value={tradeEnd ? tradeEnd : ''}
                                    onChange={(event) => setTradeEnd(event.target.value)}
                                    onFocus={() => setWarningTradeMessage("")}
                                />
                            </label>
                            <button style={{ minHeight: 'auto' }} className="btn btn-neutral ml-2 h-6" onClick={() => getNHDCTradeData()}>검색</button>
                            <div className="ml-4 text-red-500 font-bold text-sm">{warningTradeMessage}</div>
                        </div>
                    </div>
                    {tradeData && <LineChart tradeData={tradeData} tradeMaxValue={tradeMaxValue}/>}
                </div>
            </div>
        </div>
    )
}

export default NHDC; 