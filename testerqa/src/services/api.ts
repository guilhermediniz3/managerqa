import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL, 
  timeout: parseInt(process.env.REACT_APP_API_TIMEOUT || "5000"),
});

export default api;