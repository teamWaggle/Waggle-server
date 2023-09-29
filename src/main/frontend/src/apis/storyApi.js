import axios from "axios";

const url = ""; // 추후 domain 입력하고 proxy 설정 해제

export const writeStoryApi = (payload) => {
  return axios.post(`${url}/api/story/write`, payload);
};

export const editStoryApi = (boardId, payload) => {
  return axios.post(`${url}/api/story/edit/${boardId}`, payload);
};

export const getAllStoriesApi = () => {
  return axios.get(`${url}/api/story/all`);
};

export const getMemberStoriesApi = (username) => {
  return axios.get(`${url}/api/story/${username}`);
};

export const getStoryApi = (username, boardId) => {
  return axios.get(`${url}/api/story/${username}/${boardId}`);
};