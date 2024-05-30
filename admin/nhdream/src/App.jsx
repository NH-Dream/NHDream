import './App.css'
import { RouterProvider, createBrowserRouter, Route } from 'react-router-dom';
import LoginPage from '@/pages/login/LoginPage';
import SecondaryLoginPage from './pages/login/SecondaryPassword';
import DashBoardPage from '@/pages/dashboard/DashBoardPage';
import ReviewPage from '@/pages/review/ReviewPage';
import MonitorPage from '@/pages/monitor/MonitorPage';
import WalletPage from '@/pages/wallet/WalletPage';
import ProductPage from '@/pages/product/ProductPage';
import VoucherPage from '@/pages/voucher/VoucherPage';
import Farmer from '@components/review/detail/Farmer';
import Training from '@components/review/detail/Training';
import Loan from '@components/review/detail/Loan';
import NHDC from '@components/monitor/detail/NHDC';
import DRDC from '@components/monitor/detail/DRDC';
import Wallet from '@components/wallet/detail/Wallet';

function App() {
  const router = createBrowserRouter([
    {
      path: '/',
      element: <ReviewPage />,
      children: [
        {
          path: '',
          element: <Loan />,
        }, 
        {
          path: 'training',
          element: <Training />,
        }, 
        {
          path: 'farmer',
          element: <Farmer />,
        },
      ],  
    },
    {
      path: '/login',
      element: <LoginPage/>
    },
    {
      path: '/login/second',
      element: <SecondaryLoginPage/>
    },
    {
      path: '/monitoring',
      element: <MonitorPage />,
      children: [
        {
          path: '',
          element: <NHDC />,
        }, 
        {
          path: 'drdc',
          element: <DRDC />,
        },
      ],
    },
    {
      path: '/wallet',
      element: <WalletPage />,
      children: [
        {
          path: '',
          element: <Wallet />,
        },
      ],
    },
    {
      path: '/product',
      element: <ProductPage />,
    },
    {
      path: '/voucher',
      element: <VoucherPage />,
    },
  ]);

  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}

export default App