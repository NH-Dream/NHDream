

const FarmerData = ({data, index, changeModalStatus, activeIndex}) => {

    return(
        <>
            <tr className="hover" 
                onClick={()=> changeModalStatus(data.farmerReviewId, data.approvalStatus)}
            >
                <th>{data.curReviewCnt}</th>
                <td>{data.userName}</td>
                <td>{data.licenseNum}</td>
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

export default FarmerData;