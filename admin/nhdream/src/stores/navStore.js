import { create } from 'zustand'
import {persist} from "zustand/middleware";

const navStore = create(
  persist(
    (set) => ({
      curPosition: '',
      setCurPosition: (curPosition) => set({ curPosition }),
    }),
    {
      name: 'navInfo',
    }
  ));

export { navStore };
