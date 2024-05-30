import { useNavigate } from 'react-router-dom';

const NavigatorLink = ({label, link, onClick, clicked}) => {
    
    const navigate = useNavigate();

    const clickNav = () => {
        navigate(link)
        onClick()
    }; 

    return(
        <div>
            <div 
                className={`mr-8 text-base font-semibold ${clicked ? 'text-[#42C3A8]' : 'text-black'}`} 
                onClick={() => clickNav()}
            >
                {label}
            </div>
        </div>
    )
}

export default NavigatorLink;