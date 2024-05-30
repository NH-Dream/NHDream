import { create } from 'zustand'
import {persist} from "zustand/middleware";
import secureLocalStorage from 'react-secure-storage';
import Swal from 'sweetalert2';
// import { useState, useEffect } from 'react';

// // 타입 선언하기
// type UserState = {
//   id: number;
//   name: string;
//   birth:string;
//   email: string;
//   password:password,
//   walletPassword:password,
//   // ...추가적으로 필요한 정보 적으시면 됩니다!
// };

// const userStore = create('user', (set) => ({
//   // userState: {} as UserState,
//   userState: {},
//   getUserState: () => userStore().userState,
//   setUserState: (state) => set(() => ({ userState: state })),
//   // setUserState: (state) => { 
//   //   if (!state.id || !state.name || !state.birth || !state.email || !state.password) {
//   //     throw new Error('유효하지 않은 사용자 정보입니다.')
//   //   } 
//   //     set((prev) => ({ ...prev, userState: state }))},
//   // 아래는 특정 요소값 변경(필요하면 쓰십셔) - 지금은 지갑비밀번호 변경으로 설정했습니다.
//   // updateUserState: (imWalletPassword) => set((prev) => ({ ...prev, userState: { ...prev.userState, walletPassword:imWalletPassword } })),
// }))

// 회원가입을 위한 저장소
const useStore = create((set) => ({
  loginId: '',
  email: '',
  password: '',
  name: '',
  phone: '',
  birthDate: '',
  walletPassword: '',
  setLoginId: (loginId) => set({ loginId }),
  setEmail: (email) => set({ email }),
  setPassword: (password) => set({ password }),
  setName: (name) => set({ name }),
  setPhone: (phone) => set({ phone }),
  setBirthDate: (birthDate) => set({ birthDate }),
  setWalletPassword: (walletPassword) => set({ walletPassword }),
}));

// 모달 재사용
const modalStore = create((set) => ({
  successModal: (message) => {
    Swal.fire({
      icon: "success",
      title: `${message}`,
      confirmButtonColor: "#559791",
      confirmButtonText: "확인"
    });
  },
  failModal: (message) => {
    Swal.fire({
      icon: "error",
      title: `${message}`,
      confirmButtonColor: "#559791",
      confirmButtonText: "확인"
    });
  }
}));

// 로그인 관리
const secureStorage = {
  getItem: (key) => {
    return secureLocalStorage.getItem(key);
  },
  setItem: (key, value) => {
    secureLocalStorage.setItem(key, value);
  },
  removeItem: (key) => {
    secureLocalStorage.removeItem(key);
  }
};

const userStore = create(
  persist(
    (set) => ({
      // 로그인 여부 관리
      isLoggedIn: false,
      login: () => set({ isLoggedIn: true }),
      logout: () =>
        set({
          isLoggedIn: false,
          userId: null,
          userName: null,
          userType: null,
          walletAddress: null,
          userPhone: null, 
      }),
      // 유저 PK
      userId: null,
      setUserId: (userId) => set({ userId: userId }),
      // 유저 이름
      userName: null,
      setUserName: (userName) => set({ userName: userName }),
      // 유저 타입
      userType: null,
      setUserType: (userType) => set({ userType: userType }),
      // 지갑주소
      walletAddress: null,
      setWalletAddress: (walletAddress) => set({ walletAddress: walletAddress }),
      // 유저 전화번호
      userPhone: null,
      setUserPhone: (userPhone) => set({ userPhone: userPhone }),
    }),
    {
      name: 'userInfo',
      storage: secureStorage,
    }
  ))

  const useSSEStore = create((set) => ({
    lastEventId: '0',
    setLastEventId: (newLastEventId) => set({ lastEventId: newLastEventId }),
}));


export { useStore, userStore, modalStore,useSSEStore };

// component에서 사용하는 법
// import userStore from "../store/userStore";
//   const { userState, getUserState, setUserState, updateUserState } = userStore()
// 유저정보는 userState 그대로 가져와서 쓰면 되고, (userState.name이러면 userState에 저장된 name이 불러와진다.)
// 유저정보 세팅은 setUserState에서 setUserState(유저 정보) 이런식으로 넣어주면 됩니다.