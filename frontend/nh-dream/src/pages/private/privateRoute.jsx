import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { userStore } from '../../stores/userStore';

const PrivateRoute = () => {
  const { isLoggedIn } = userStore();

  if (!isLoggedIn) {
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};

export default PrivateRoute;