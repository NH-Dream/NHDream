/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        LOGIN: '#E6FEED',
        CONTENT: '#F5F8F6',
        SELECTED: '#42C3A8',
        APPROVAL: '#C4ECE4',
        REJECT: '#FFB9AF',
        DETAIL: '#EEF3EF',
      },
      spacing: {
        SIDEBAR: '255px',
        HEIGHT: '600px'
      },
      height: {
        '20': '20%',
        '30': '35%',
        '40': '45%',
        '60': '60%',
        '80': '80%',
      }, 
    },
  },
  plugins: [require("daisyui")],
}