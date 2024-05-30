import NavigatorLink from "@components/@common/NavigatorLink/NavigatorLink"
import { navStore } from "@/stores/navStore";

const Navigator = ({links}) => {
    const store = navStore(); 

    const changeTag = (name) => {
        store.setCurPosition(name)
    }

    return(
        <div className="flex items-center w-5/6 h-10 border-b border-black" >
            <div className="flex ml-2">
                {links.map((link, index) => (
                    <NavigatorLink 
                        key={index} 
                        label={link.label} 
                        link={link.link}
                        onClick={() => changeTag(link.label)}
                        clicked={store.curPosition === link.label}
                    />
                ))}
            </div>
        </div>
    )
}

export default Navigator;