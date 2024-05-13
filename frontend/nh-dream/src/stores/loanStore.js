import { create } from 'zustand'

const loanStore = create((set) => ({
  optionId:'',
  setOptionId: (optionId) => set({ optionId }),
}))

export { loanStore };