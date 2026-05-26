# API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require JWT token in Authorization header:
```
Authorization: Bearer <token>
```

## Endpoints

### Authentication APIs

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phone": "1234567890",
  "role": "JOB_SEEKER",
  "companyName": "Tech Corp",  // Required for RECRUITER
  "companyWebsite": "https://techcorp.com"  // Optional for RECRUITER
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "JOB_SEEKER"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "fullName": "John Doe",
  "role": "JOB_SEEKER"
}
```

### Job APIs

#### Get All Jobs
```http
GET /jobs?page=0&size=10
Authorization: Bearer <token>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "title": "Senior Java Developer",
      "description": "We are looking for...",
      "location": "New York, NY",
      "jobType": "FULL_TIME",
      "experienceLevel": "SENIOR_LEVEL",
      "salaryRange": "$120,000 - $150,000",
      "minExperience": 5,
      "maxExperience": 8,
      "requiredSkills": ["Java", "Spring Boot", "MySQL"],
      "companyName": "Tech Corp",
      "recruiterId": 2,
      "status": "ACTIVE",
      "postedAt": "2024-01-15T10:30:00",
      "closingDate": "2024-02-15T23:59:59"
    }
  ],
  "totalPages": 5,
  "totalElements": 50,
  "size": 10,
  "number": 0
}
```

#### Search Jobs
```http
GET /jobs/search?keyword=java&page=0&size=10
Authorization: Bearer <token>

Response: 200 OK (same structure as Get All Jobs)
```

#### Get Job by ID
```http
GET /jobs/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "title": "Senior Java Developer",
  "description": "We are looking for...",
  ...
}
```

### Job Seeker APIs

#### Apply for Job
```http
POST /jobseeker/apply/{jobId}?coverLetter=I am interested...
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "userId": 1,
  "applicantName": "John Doe",
  "applicantEmail": "john@example.com",
  "jobId": 1,
  "jobTitle": "Senior Java Developer",
  "status": "APPLIED",
  "coverLetter": "I am interested...",
  "matchScore": 87.5,
  "atsScore": 92.0,
  "rankPosition": 3,
  "missingSkills": "aws, docker",
  "appliedAt": "2024-01-20T14:30:00"
}
```

#### Get My Applications
```http
GET /jobseeker/applications?page=0&size=10
Authorization: Bearer <token>

Response: 200 OK
{
  "content": [
    {
      "id": 1,
      "userId": 1,
      "applicantName": "John Doe",
      "jobId": 1,
      "jobTitle": "Senior Java Developer",
      "status": "APPLIED",
      "matchScore": 87.5,
      "atsScore": 92.0,
      "rankPosition": 3,
      "appliedAt": "2024-01-20T14:30:00"
    }
  ],
  "totalPages": 2,
  "totalElements": 15
}
```

#### Upload Resume
```http
POST /jobseeker/resume/upload
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: [PDF file]

Response: 200 OK
{
  "skills": ["Java", "Spring Boot", "MySQL", "Docker"],
  "education": ["Bachelor of Computer Science"],
  "experience": ["Software Engineer at Tech Corp"],
  "certifications": ["AWS Certified Developer"],
  "atsScore": 92.5,
  "summary": "Experienced software engineer...",
  "metadata": {
    "wordCount": 450,
    "skillCount": 15
  }
}
```

### Recruiter APIs

#### Create Job
```http
POST /recruiter/jobs
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Senior Java Developer",
  "description": "We are looking for an experienced Java developer...",
  "location": "New York, NY",
  "jobType": "FULL_TIME",
  "experienceLevel": "SENIOR_LEVEL",
  "salaryRange": "$120,000 - $150,000",
  "minExperience": 5,
  "maxExperience": 8,
  "requiredSkills": ["Java", "Spring Boot", "MySQL", "Docker"],
  "closingDate": "2024-02-15T23:59:59"
}

Response: 200 OK
{
  "id": 1,
  "title": "Senior Java Developer",
  ...
}
```

#### Get Ranked Candidates
```http
GET /recruiter/jobs/{jobId}/candidates
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "userId": 5,
    "applicantName": "Jane Smith",
    "applicantEmail": "jane@example.com",
    "jobId": 1,
    "jobTitle": "Senior Java Developer",
    "status": "APPLIED",
    "matchScore": 95.5,
    "atsScore": 94.0,
    "rankPosition": 1,
    "missingSkills": "",
    "appliedAt": "2024-01-20T10:00:00"
  },
  {
    "id": 2,
    "userId": 7,
    "applicantName": "Bob Johnson",
    "applicantEmail": "bob@example.com",
    "matchScore": 87.5,
    "atsScore": 92.0,
    "rankPosition": 2,
    "missingSkills": "kubernetes",
    "appliedAt": "2024-01-20T11:30:00"
  }
]
```

## Error Responses

### 400 Bad Request
```json
{
  "email": "Invalid email format",
  "password": "Password must be at least 6 characters"
}
```

### 401 Unauthorized
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2024-01-20T14:30:00"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Job not found",
  "timestamp": "2024-01-20T14:30:00"
}
```

### 409 Conflict
```json
{
  "status": 409,
  "message": "Email already registered",
  "timestamp": "2024-01-20T14:30:00"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "An error occurred: ...",
  "timestamp": "2024-01-20T14:30:00"
}
```

## Rate Limiting
- 100 requests per minute per IP
- 1000 requests per hour per user

## Pagination
All list endpoints support pagination:
- `page`: Page number (0-indexed)
- `size`: Items per page (default: 10, max: 100)

## Swagger UI
Interactive API documentation available at:
```
http://localhost:8080/swagger-ui.html
```
