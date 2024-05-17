/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Pretendard SemiBold', 'Pretendard Light', 'Pretendard ExtraBold', 'sans-serif'],
      },
      fontWeight: {
        light: 200,
        semibold: 500,
        extrabold: 800,
      },
  
    },
  },
  plugins: [require("daisyui")],
}
