

const LoanData = ({data, index, changeModalStatus, activeIndex}) => {

    return(
        <>
            <tr className="hover" onClick={()=> changeModalStatus(data.loanReviewId, data.approvalStatus)}>
                <th>{data.curReviewCnt}</th>
                <td>{data.productName}</td>
                <td>{data.userName}</td>
                <td>{data.loanAmount.toLocaleString()}</td>
                <td>{data.interestRate}</td>
                <td>{data.applicationDate}</td>
                <td className="flex justify-center items-center">
                    <div className={`badge ${data.approvalStatus === 'APPROVED' ? 'bg-APPROVAL text-[#1E7572] font-bold' : data.approvalStatus === 'PENDING' ? 'bg-[#A2A2A2] font-bold' : 'bg-REJECT text-[#FF0000] font-bold'}`}>
                        {data.approvalStatus}
                    </div>
                </td>
            </tr>
        </>
    )
}

export default LoanData;