import axios from "axios";

const url = ""; // 추후 domain 입력하고 proxy 설정 해제


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