// import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { userStore } from '../../stores/userStore';

const PrivateRoute = () => {
  // const { isLoggedIn } = userStore();
  const store = userStore()

  const isAccess = localStorage.getItem("access")

  if (!isAccess) {
    store.logout()
    return <Navigate to="/login" />;
  }

  return <Outlet />;
};

export default PrivateRoute;