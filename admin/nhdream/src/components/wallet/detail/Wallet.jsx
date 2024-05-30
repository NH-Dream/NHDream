import { individualWallet } from "@/services/wallet";
import WalletCard from "@components/@common/card/WalletCard";
import { getInterestWalletBalance, totalBurned, totalMinted } from "../../../../blockchain/NHDC";
import { useEffect, useState } from "react";

const Wallet = () => {
    const [mint, setMint] = useState(0)
    const [burn, setBurn] = useState(0)
    const [smallWallet, setSmallWallet] = useState(0)
    const [interestWallet, setInterestWallet] = useState(0)
    const [individual, setIndividual] = useState(0)

    useEffect(() => {
        const interval = setInterval(() => {
            individualWallet(
                res => {
                    setIndividual(res.count)
                },
                err => {
                    console.log("개인지갑불러오기 에러발생", err)
                }
            )
    
            getInterestWalletBalance(
                res => {
                    console.log('res : ', res)
                    setInterestWallet(res)
                },
                err => console.log('err : ', err)
            )
    
            totalMinted(
                res => {
                    setMint(res)
                    console.log('mint_res : ', res)
                },
                err => {
                    console.log("전체양 불러오기 실패", err)
                }
            )
            
            totalBurned(
                res => {
                    setBurn(res)
                    console.log('burn_res : ', res)
                },
                err => {
                    console.log("소각량 불러오기 실패", err)
                }
            )
        }, 5000)
        return () => {
            clearInterval(interval)
        }
    }, [])

    useEffect(() => {
            individualWallet(
                res => {
                    setIndividual(res.count)
                },
                err => {
                    console.log("개인지갑불러오기 에러발생", err)
                }
            )
    
            getInterestWalletBalance(
                res => {
                    console.log('res : ', res)
                    setInterestWallet(res)
                },
                err => console.log('err : ', err)
            )
    
            totalMinted(
                res => {
                    setMint(res)
                    console.log('mint_res : ', res)
                },
                err => {
                    console.log("전체양 불러오기 실패", err)
                }
            )
            
            totalBurned(
                res => {
                    setBurn(res)
                    console.log('burn_res : ', res)
                },
                err => {
                    console.log("소각량 불러오기 실패", err)
                }
            )
    }, [])

    useEffect(() => {
        setSmallWallet(Number(mint)-Number(burn))
    }, [mint, burn])

    return (
        <div className="flex justify-center a w-full h-full bg-CONTENT">
            <div className=" p-4 w-5/6 h-40 bg-DETAIL mt-5">
                <div>
                    <p className="text-left text-xs font-bold">NHDC 지갑</p>
                    <div className="w-full h-24 flex mt-5 ">
                        <WalletCard title="소액 지갑" amount={smallWallet} address="0x1D0C65C898334649e960031577B4D0FcD5661Aa7" />
                        <WalletCard title="이자 지갑" amount={interestWallet.toLocaleString()} address="0x318473d430D02b1Fe0051C6700b9850A60dDac0b " />
                    </div>
                </div>
                <div className="mt-7">
                    <p className="text-left text-xs font-bold">당행 지갑</p>
                    <div className="w-full h-24 flex mt-5">
                        <WalletCard title="개인 지갑" amount={individual.toLocaleString()} address="" />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Wallet; 
