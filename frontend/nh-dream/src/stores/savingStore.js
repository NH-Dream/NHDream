import { create } from 'zustand'
// import { useState, useEffect } from 'react';

// 적금 목록 저장소
const savingStore = create((set) => ({
  id: '',
  name: '',
  graduateRate6Months: '',
  graduateRate12Months: '',
  graduateRate24Months: '',
  farmerRate6Months: '',
  farmerRate12Months: '',
  farmerRate24Months: '',
  minRate: '',
  maxRate: '',
  option6Id: '',
  option12Id: '',
  option24Id: '',
  setId: (id) => set({ id }),
  setName: (name) => set({ name }),
  // setEmail: (email) => set({ email }),
  // setPassword: (password) => set({ password }),
  // setPhone: (phone) => set({ phone }),
  // setBirthDate: (birthDate) => set({ birthDate }),
  // setWalletPassword: (walletPassword) => set({ walletPassword }),
}));

export { savingStore };

// component에서 사용하는 법
// import userStore from "../store/userStore";
//   const { userState, getUserState, setUserState, updateUserState } = userStore()
// 유저정보는 userState 그대로 가져와서 쓰면 되고, (userState.name이러면 userState에 저장된 name이 불러와진다.)
// 유저정보 세팅은 setUserState에서 setUserState(유저 정보) 이런식으로 넣어주면 됩니다.