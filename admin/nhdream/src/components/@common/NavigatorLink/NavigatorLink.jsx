import { useNavigate } from 'react-router-dom';

const NavigatorLink = ({label, link}) => {
    const navigate = useNavigate();

    return(
        <div>
            <div onClick={() => navigate(link)}>
                {label}
            </div>
        </div>
    )
}

export default NavigatorLink;