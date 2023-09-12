import axios from "axios";

const url = "http://localhost:8080";


export const memberRegisterApi = (payload) => {
  return axios.post(`${url}/api/member/register`, payload);
};

export const memberLogoutApi = () => {
  return axios.post(`${url}/api/member/logout`);
};

export const memberLoginApi = (payload) => {
  return axios.post(`${url}/api/member/login`, payload);
};

export const getSimpleMemberInfo = (username) => {
  return axios.get(`${url}/api/member/${username}`);
};