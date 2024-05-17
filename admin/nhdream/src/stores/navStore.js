import { create } from 'zustand'
import {persist} from "zustand/middleware";

const navStore = create(
  persist(
    (set) => ({
      curSideBar: '', 
      curPosition: '',
      setCurSideBar: (curSideBar) => set({ curSideBar }),
      setCurPosition: (curPosition) => set({ curPosition }),
    }),
    {
      name: 'navInfo',
    }
  ));

export { navStore };
