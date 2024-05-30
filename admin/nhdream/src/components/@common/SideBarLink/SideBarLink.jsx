import { useNavigate } from 'react-router-dom';
import { navStore } from "@/stores/navStore";

const SideBarLink = ({label, link, curPosition}) => {
    const navigate = useNavigate();
    const store = navStore();

    const changeCategory = (name) => {
        navigate(link)
        store.setCurPosition(name)
        store.setCurSideBar(label); 
    }

    return (
        <div>
            <div 
                className={`text-xl ${label === store.curSideBar ? 'text-[#42C3A8]' : 'text-black'}`} 
                onClick={() => changeCategory(curPosition)}
            >
                {label}
            </div>
        </div>   
    );
};

export default SideBarLink;