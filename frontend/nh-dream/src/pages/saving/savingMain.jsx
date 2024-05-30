import { useState,useEffect } from "react"
import { useLocation } from "react-router-dom";
import { SavingMainComponent, DepositMainComponent } from "../../components/saving/index"

export default function SavingMainPage() {
  const location = useLocation();
  const [selected, setSelected] = useState('deposite')
  const renderComponent = () => {
    switch (selected) {
      case 'deposite':
        return <DepositMainComponent />;
      case 'saving':
        return <SavingMainComponent />;
    }
  };

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const isSaving = searchParams.get('saving');
    if (isSaving) {
      setSelected('saving');
    }
  }, [location]);

  
  return (
    <div className="mt-3">
      <div className="h-12 flex justify-between px-20" style={{ borderBottom: '2px solid #ccc' }}>
        <div className="flex flex-col justify-end" onClick={() => setSelected('deposite')}>
          <div className="font-bold text-xl">예금</div>
          <div className="h-1.5" style={{
            backgroundColor: selected === 'deposite' ? '#2EB1AD' : 'transparent',
            borderRadius: '15px 15px 0 0'
          }}></div>
        </div>
        <div className="flex flex-col justify-end" onClick={() => setSelected('saving')}>
          <div className="font-bold text-xl">적금</div>
          <div className="h-1.5" style={{
            backgroundColor: selected === 'saving' ? '#2EB1AD' : 'transparent',
            borderRadius: '15px 15px 0 0'
          }}></div>
        </div>
      </div>
      {renderComponent()}
    </div>
  )
}