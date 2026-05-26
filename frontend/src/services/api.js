import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Auth APIs
export const register = (data) => api.post('/auth/register', data);
export const login = (data) => api.post('/auth/login', data);

// Job APIs
export const getJobs = (page = 0, size = 10) => api.get(`/jobs?page=${page}&size=${size}`);
export const searchJobs = (keyword, page = 0, size = 10) => 
  api.get(`/jobs/search?keyword=${keyword}&page=${page}&size=${size}`);
export const getJobById = (id) => api.get(`/jobs/${id}`);

// Job Seeker APIs
export const applyForJob = (jobId, coverLetter) => 
  api.post(`/jobseeker/apply/${jobId}`, null, { params: { coverLetter } });
export const getMyApplications = (page = 0, size = 10) => 
  api.get(`/jobseeker/applications?page=${page}&size=${size}`);
export const uploadResume = (file) => {
  const formData = new FormData();
  formData.append('file', file);
  return api.post('/jobseeker/resume/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};

// Recruiter APIs
export const createJob = (jobData) => api.post('/recruiter/jobs', jobData);
export const getRankedCandidates = (jobId) => api.get(`/recruiter/jobs/${jobId}/candidates`);

export default api;
