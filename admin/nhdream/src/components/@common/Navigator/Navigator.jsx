import NavigatorLink from "@components/@common/NavigatorLink/NavigatorLink"

const Navigator = ({links}) => {

    return(
        <div className="flex w-40 h-10 border-b-2" >
            {links.map((link, index) => (
                <NavigatorLink key={index} label={link.label} link={link.link} />
            ))}
        </div>
    )
}

export default Navigator;