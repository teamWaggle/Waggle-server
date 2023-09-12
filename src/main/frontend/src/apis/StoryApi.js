import axios from "axios";

const url = "http://localhost:8080";

export const writeStoryApi = (payload) => {
  return axios.post(`${url}/api/story/write`, payload);
};

export const editStoryApi = (boardId, payload) => {
  return axios.post(`${url}/api/story/edit/${boardId}`, payload);
};

export const getMemberStoriesApi = (username) => {
  return axios.get(`${url}/api/story/${username}`);
};

export const getStoryApi = (username, boardId) => {
  return axios.get(`${url}/api/story/${username}/${boardId}`);
};