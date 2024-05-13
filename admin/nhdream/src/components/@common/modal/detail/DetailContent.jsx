
const DetailContent = ({label, content}) => {
    
    return(
        <div className='border-b-2'>
            <div className="text-xs my-1">{label}</div>
            <div className="font-bold ml-2 mb-1">{content}</div>
        </div>
)
}

export default DetailContent; 