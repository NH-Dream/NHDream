import { useNavigate } from 'react-router-dom';
import { navStore } from "@/stores/navStore";

const SideBarLink = ({label, link, curPosition}) => {
    const navigate = useNavigate();
    const store = navStore();

    const changeCategory = (name) => {
        navigate(link)
        store.setCurPosition(name)
    }

    return (
        <div>
            <div onClick={() => changeCategory(curPosition)}>
                {label}
            </div>
        </div>   
    );
};

export default SideBarLink;