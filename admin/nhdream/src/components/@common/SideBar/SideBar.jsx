import { useState } from "react"
import { useNavigate } from "react-router-dom"
import SideBarLink from "@components/@common/SideBarLink/SideBarLink"
import NHdreamLogo from "@/assets/images/nh_dream_dashBoard/NHdreamGuest.png"
import me from "@/assets/images/nh_dream_dashBoard/me.png"

import REVIEWBLACK from "@/assets/images/nh_dream_dashBoard/review_black.png"
import MONITORBLACK from "@/assets/images/nh_dream_dashBoard/monitor_black.png"
import WALLETBLACK from "@/assets/images/nh_dream_dashBoard/wallet_black.png"
import PRODUCTBLACK from "@/assets/images/nh_dream_dashBoard/product_black.png"
import VOUCHERBLACK from "@/assets/images/nh_dream_dashBoard/voucher_black.png"
import logout from "@/assets/images/nh_dream_dashBoard/logout.png"
import REVIEWGREEN from "@/assets/images/nh_dream_dashBoard/review_green.png"
import MONITORGREEN from "@/assets/images/nh_dream_dashBoard/monitor_green.png"
import WALLETGREEN from "@/assets/images/nh_dream_dashBoard/wallet_green.png"
import PRODUCTGREEN from "@/assets/images/nh_dream_dashBoard/product_green.png"
import VOUCHERGREEN from "@/assets/images/nh_dream_dashBoard/voucher_green.png"

import "@/assets/css/test.css"
import "@/assets/font/font.css"

import { navStore } from "@/stores/navStore";

const SideBar = () => {
    const navigate = useNavigate()
    const logoutPage = () => {
        navigate('/login')
    }
    const [name, setName] = useState("권 현 준")
    const [department, setDepartment] = useState("금융상품 모니터링팀")
    const store = navStore();

    return (
        <div className="h-full w-SIDEBAR" >
            <div className=" flex flex-col items-center my-5">
                <div className="w-5/6 mb-5">
                    <img src={NHdreamLogo} alt="NH dream Logo" />
                </div>
                <div className="flex flex-col items-center">
                    <img src={me} alt="프로필 사진" />
                    <p className="mt-3 font-bold">{name}</p>
                    <p className="font">{department}</p>
                </div>
            </div>

            <div className="flex flex-col ml-5 gap-3 font-bold mt-7">
                <div className="flex items-center gap-3.5">
                    {store.curSideBar == "심    사" ? 
                        (<img style={{height:"25px", width:"25px"}} src={REVIEWGREEN} alt="심사" />) :
                        (<img style={{height:"25px", width:"25px"}} src={REVIEWBLACK} alt="심사" />)
                    }
                    <SideBarLink label="심    사" link="/" curPosition="대 출" />
                </div>
                <div className="flex items-center gap-3.5 mt-3">
                    {store.curSideBar == "모니터링" ? 
                            (<img style={{height:"25px", width:"25px"}} src={MONITORGREEN} alt="모니터링" />) :
                            (<img style={{height:"25px", width:"25px"}} src={MONITORBLACK} alt="모니터링" />)
                    }
                    <SideBarLink label="모니터링" link="/monitoring" curPosition="NHDC"></SideBarLink>
                </div>
                <div className="flex items-center gap-3.5 mt-3">
                    {store.curSideBar == "지갑관리" ? 
                            (<img style={{height:"25px", width:"25px"}} src={WALLETGREEN} alt="지갑관리" />) :
                            (<img style={{height:"25px", width:"25px"}} src={WALLETBLACK} alt="지갑관리" />)
                    }
                    <SideBarLink label="지갑관리" link="/wallet" curPosition="당행 전자지갑관리" ></SideBarLink>
                </div>
                <div className="flex items-center gap-3.5 mt-3">
                    {store.curSideBar == "상품관리" ? 
                            (<img style={{height:"25px", width:"25px"}} src={PRODUCTGREEN} alt="상품관리" />) :
                            (<img style={{height:"25px", width:"25px"}} src={PRODUCTBLACK} alt="상품관리" />)
                    }
                    <SideBarLink label="상품관리" link="/product" curPosition="전체 현황"></SideBarLink>
                </div>
                <div className="flex items-center gap-3.5 mt-3">
                    {store.curSideBar == "바우처" ? 
                            (<img style={{height:"25px", width:"25px"}} src={VOUCHERGREEN} alt="바우처" />) :
                            (<img style={{height:"25px", width:"25px"}} src={VOUCHERBLACK} alt="바우처" />)
                    }
                    <SideBarLink label="바우처" link="/voucher" curPosition=""></SideBarLink>
                </div>
            </div>

            <div className="flex justify-start items-center ml-5 gap-5 logout">
                <img src={logout} alt="" />
                <div className="font" onClick={() => logoutPage()}>Logout</div>
            </div>
        </div>
    );
};

export default SideBar;