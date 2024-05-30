
const TokenCard = ({title, amount}) => {

  // 숫자를 한글로 변환하는 함수
  function numberToKorean(amount) {
    const numKor = ["", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"];
    const danKor = ["", "십", "백", "천", "", "십", "백", "천", "", "십", "백", "천", "", "십", "백", "천"];
    const unitWords = ["", "만", "억", "조", "경"];

    let result = "";
    const amountStr = amount.toString();
    const length = amountStr.length;
    
    for (let i = 0; i < length; i++) {
      const digit = amountStr[length - 1 - i];  // 각 자리 숫자
      const num = numKor[parseInt(digit)];  // 해당 숫자의 한글 표현
      const dan = danKor[i % 4];  // 자리수에 해당하는 한글 단위
      let str = num + dan;

      if (i % 4 === 0 && i > 0) {  // 만, 억, 조, 경 단위 추가
        str += unitWords[Math.floor(i / 4)];
      }

      result = str + result;
    }

    // 불필요한 단위 제거
    result = result.replace(/일(?=[만억조경])/g, '');
    
    return result;
  }



    const koreanAmount = (amount == 0) ? "영" : numberToKorean(amount);

    return(
        <div className="w-80 bg-base-100 rounded mr-4 last:mr-0">
            <div className="flex flex-col h-full p-3 font-bold">
                <p className="text-left text-xs">{title}</p>
                <div className="flex-grow"></div> {/* 이 요소가 나머지 공간을 차지하게 하여 하단 요소를 밑으로 밀어냅니다. */}
                <div className="text-right mt-auto"> {/* mt-auto를 사용하여 하단 정렬, mb-2로 하단 여백 */}
                    <div className="flex justify-end items-end">
                        <div className="text-xl">{amount.toLocaleString()}</div><div className="ml-2 text-xs">원</div>
                    </div>
                    <div className="flex justify-end text-[9px] mt-1">
                        <div>{koreanAmount}</div><div>원</div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default TokenCard;