import axios from 'axios';

const api = axios.create({
  baseURL : 'https://k10s209.p.ssafy.io/api/',
  // baseURL: "http://localhost:8080/api/",
})

export default api;