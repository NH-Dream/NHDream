import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { userStore, modalStore } from '../../stores/userStore';

const PrivateRoute = () => {
  const { isLoggedIn } = userStore();
  const { failModal } = modalStore();

  // if (!isLoggedIn) {
  //   failModal("로그인이 필요합니다.");
  //   return <Navigate to="/login" />;
  // }

  return <Outlet />;
};

export default PrivateRoute;