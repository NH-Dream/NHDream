import { useNavigate } from "react-router-dom"
import { userStore } from "@/stores/userStore";
// import { loanStore } from "@/stores/loanStore";
import { LoanJudge } from '@/services/loan'
import { useState } from "react";

export default function LoanJudgeComponent({response}) {
  // const params = useParams()
  const navigate = useNavigate()
  const store = userStore()
  const formData = new FormData();
  
  const goToJudge = (formDoc) => {
    LoanJudge(
      store.userId,
      response,
      formDoc,
      res => {
        navigate('/loan/joinJudge/done')
      },
      err => {
        console.error("가입신청 실패", err)
      }
    )
  }

  const makeForm = async () => {
    if (identity && farmland && income && nationalTax && localTax) {
      await putForm(identity,farmland,income,nationalTax,localTax)
      goToJudge(formData)
    }
  }
  
  const putForm = (identity,farmland,income,nationalTax,localTax) => {
    formData.append('files', identity)
    formData.append('files', farmland)
    formData.append('files', income)
    formData.append('files', nationalTax)
    formData.append('files', localTax)
  }

  const [identity, setIdentity] = useState(null)
  const [farmland, setFarmland] = useState(null)
  const [income, setIncome] = useState(null)
  const [nationalTax, setNationalTax] = useState(null)
  const [localTax, setLocalTax] = useState(null)

  return (
    <div className="w-11/12 m-auto">
      <div className="flex flex-col">
        <div className="my-3">
          <label className="form-control w-full">
            <div className="label">
              <span className="label-text">신분증</span>
            </div>
            <input type="file" className="file-input file-input-bordered file-input-accent w-full" onChange={(event) => setIdentity(event.target.files[0])}/>
          </label>
        </div>
        <div className="my-3">
          <label className="form-control w-full">
            <div className="label">
              <span className="label-text">농지취득자격증명</span>
            </div>
            <input type="file" className="file-input file-input-bordered file-input-accent w-full" onChange={(event) => setFarmland(event.target.files[0])}/>
          </label>
        </div>
        <div className="my-3">
          <label className="form-control w-full">
            <div className="label">
              <span className="label-text">소득금액증명원</span>
            </div>
            <input type="file" className="file-input file-input-bordered file-input-accent w-full" onChange={(event) => setIncome(event.target.files[0])}/>
          </label>
        </div>
        <div className="my-3">
          <label className="form-control w-full">
            <div className="label">
              <span className="label-text">국세완납증명서</span>
            </div>
            <input type="file" className="file-input file-input-bordered file-input-accent w-full" onChange={(event) => setNationalTax(event.target.files[0])}/>
          </label>
        </div>
        <div className="my-3">
          <label className="form-control w-full">
            <div className="label">
              <span className="label-text">지방세완납증명서</span>
            </div>
            <input type="file" className="file-input file-input-bordered file-input-accent w-full" onChange={(event) => setLocalTax(event.target.files[0])}/>
          </label>
        </div>
      </div>
      <div
        className="flex justify-center mt-6"
        onClick={() => makeForm()}
      >
        <button className="savingJoinBtn w-full">대출 심사 신청하기</button>
      </div>
    </div>
  )
}