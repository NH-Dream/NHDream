import { useEffect, useState } from "react";

const TokenCard = ({title, amount, address}) => {

    const [word , setWord] = useState(""); 

    useEffect(()=>{
        if(title === "개인 지갑"){
            setWord("개")
        }else{
            setWord("원")
        }
    }, [])


    return(
        <div className="w-80 bg-base-100 rounded mr-4 last:mr-0">
            <div className="flex flex-col h-full p-3 font-bold">
                <p className="text-left text-xs">{title}</p>
                <div className="flex-grow"></div> {/* 이 요소가 나머지 공간을 차지하게 하여 하단 요소를 밑으로 밀어냅니다. */}
                <div className="text-right mt-auto"> {/* mt-auto를 사용하여 하단 정렬, mb-2로 하단 여백 */}
                    <div className="flex justify-end items-end">
                        <div className="text-xl">{amount.toLocaleString()}</div><div className="ml-2 text-xs">{word}</div>
                    </div>
                    <div className="flex justify-end text-[9px] mt-1">
                        <div>{address}</div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default TokenCard;