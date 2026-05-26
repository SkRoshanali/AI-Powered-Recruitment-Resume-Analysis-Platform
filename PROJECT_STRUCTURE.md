# Project Structure

## Complete Directory Layout

```
recruitment-platform/
│
├── README.md                          # Project overview
├── DEPLOYMENT_GUIDE.md                # Deployment instructions
├── API_DOCUMENTATION.md               # API reference
├── DATABASE_SCHEMA.md                 # Database design
├── PROJECT_STRUCTURE.md               # This file
├── docker-compose.yml                 # Docker orchestration
│
├── backend/                           # Spring Boot Backend
│   ├── Dockerfile
│   ├── pom.xml                        # Maven dependencies
│   └── src/
│       ├── main/
│       │   ├── java/com/recruitment/
│       │   │   ├── RecruitmentApplication.java    # Main application
│       │   │   │
│       │   │   ├── controller/                    # REST Controllers
│       │   │   │   ├── AuthController.java
│       │   │   │   ├── JobController.java
│       │   │   │   ├── JobSeekerController.java
│       │   │   │   └── RecruiterController.java
│       │   │   │
│       │   │   ├── service/                       # Business Logic
│       │   │   │   ├── AuthService.java
│       │   │   │   ├── JobService.java
│       │   │   │   ├── ApplicationService.java
│       │   │   │   └── AIService.java
│       │   │   │
│       │   │   ├── repository/                    # Data Access Layer
│       │   │   │   ├── UserRepository.java
│       │   │   │   ├── JobRepository.java
│       │   │   │   ├── ApplicationRepository.java
│       │   │   │   ├── JobSeekerProfileRepository.java
│       │   │   │   ├── RecruiterProfileRepository.java
│       │   │   │   └── SkillRepository.java
│       │   │   │
│       │   │   ├── entity/                        # JPA Entities
│       │   │   │   ├── User.java
│       │   │   │   ├── JobSeekerProfile.java
│       │   │   │   ├── RecruiterProfile.java
│       │   │   │   ├── Job.java
│       │   │   │   ├── Skill.java
│       │   │   │   ├── Application.java
│       │   │   │   ├── Education.java
│       │   │   │   ├── Experience.java
│       │   │   │   └── Certification.java
│       │   │   │
│       │   │   ├── dto/                           # Data Transfer Objects
│       │   │   │   ├── AuthRequest.java
│       │   │   │   ├── AuthResponse.java
│       │   │   │   ├── RegisterRequest.java
│       │   │   │   ├── JobDTO.java
│       │   │   │   ├── ApplicationDTO.java
│       │   │   │   └── ResumeAnalysisResponse.java
│       │   │   │
│       │   │   ├── security/                      # Security Configuration
│       │   │   │   ├── SecurityConfig.java
│       │   │   │   ├── JwtUtil.java
│       │   │   │   ├── JwtAuthenticationFilter.java
│       │   │   │   └── CustomUserDetailsService.java
│       │   │   │
│       │   │   └── exception/                     # Exception Handling
│       │   │       ├── GlobalExceptionHandler.java
│       │   │       ├── ResourceNotFoundException.java
│       │   │       └── ResourceAlreadyExistsException.java
│       │   │
│       │   └── resources/
│       │       ├── application.yml                # Application configuration
│       │       └── application-prod.yml           # Production config
│       │
│       └── test/                                  # Unit & Integration Tests
│           └── java/com/recruitment/
│               ├── controller/
│               ├── service/
│               └── repository/
│
├── frontend/                          # React Frontend
│   ├── Dockerfile
│   ├── package.json                   # NPM dependencies
│   ├── public/
│   │   └── index.html
│   └── src/
│       ├── index.js                   # Entry point
│       ├── index.css
│       ├── App.js                     # Main component
│       ├── App.css
│       │
│       ├── components/                # Reusable Components
│       │   └── Navigation.js
│       │
│       ├── pages/                     # Page Components
│       │   ├── Login.js
│       │   ├── Register.js
│       │   ├── JobList.js
│       │   ├── JobDetails.js
│       │   ├── Dashboard.js
│       │   └── RecruiterDashboard.js
│       │
│       └── services/                  # API Services
│           └── api.js
│
└── ai-service/                        # Python AI Service
    ├── Dockerfile
    ├── requirements.txt               # Python dependencies
    └── app.py                         # Flask application

```

## Architecture Layers

### Backend (Spring Boot)

#### 1. Controller Layer
- **Purpose**: Handle HTTP requests and responses
- **Responsibilities**:
  - Request validation
  - Response formatting
  - HTTP status codes
  - API documentation (Swagger)
- **Pattern**: RESTful API design

#### 2. Service Layer
- **Purpose**: Business logic implementation
- **Responsibilities**:
  - Core business rules
  - Transaction management
  - Data transformation
  - Integration with external services
- **Pattern**: Service-oriented architecture

#### 3. Repository Layer
- **Purpose**: Data access abstraction
- **Responsibilities**:
  - Database operations
  - Query execution
  - Data persistence
- **Pattern**: Repository pattern with Spring Data JPA

#### 4. Entity Layer
- **Purpose**: Domain model representation
- **Responsibilities**:
  - Database table mapping
  - Relationship definitions
  - Data validation
- **Pattern**: JPA/Hibernate ORM

#### 5. DTO Layer
- **Purpose**: Data transfer between layers
- **Responsibilities**:
  - API request/response objects
  - Data encapsulation
  - Validation rules
- **Pattern**: Data Transfer Object pattern

#### 6. Security Layer
- **Purpose**: Authentication and authorization
- **Responsibilities**:
  - JWT token management
  - User authentication
  - Role-based access control
  - Password encryption
- **Pattern**: Spring Security with JWT

#### 7. Exception Layer
- **Purpose**: Centralized error handling
- **Responsibilities**:
  - Exception catching
  - Error response formatting
  - Logging
- **Pattern**: Global exception handler

### Frontend (React)

#### 1. Components
- **Reusable UI elements**
- Navigation, forms, cards, etc.

#### 2. Pages
- **Route-specific views**
- Login, Dashboard, Job listings, etc.

#### 3. Services
- **API communication**
- Axios HTTP client
- Request/response handling

### AI Service (Python/Flask)

#### 1. Resume Analysis
- PDF text extraction
- NLP processing with spaCy
- Skill extraction
- ATS score calculation

#### 2. Recommendation Engine
- Job-candidate matching
- Similarity scoring
- Skill gap analysis

## Technology Stack Details

### Backend Technologies
```
Java 17
Spring Boot 3.2.0
Spring Security
Spring Data JPA
Hibernate ORM
MySQL 8.0
JWT (JSON Web Tokens)
Maven
Lombok
PDFBox (PDF processing)
Swagger/OpenAPI
```

### Frontend Technologies
```
React 18
React Router DOM
Axios
Bootstrap 5
React Bootstrap
React Icons
```

### AI/NLP Technologies
```
Python 3.11
Flask
spaCy
NLTK
Scikit-learn
NumPy
Pandas
```

### DevOps & Tools
```
Docker
Docker Compose
Git
Postman
MySQL Workbench
```

## Design Patterns Used

### 1. MVC (Model-View-Controller)
- **Model**: Entity classes
- **View**: React components
- **Controller**: Spring controllers

### 2. Repository Pattern
- Abstraction over data access
- Spring Data JPA repositories

### 3. Service Layer Pattern
- Business logic separation
- Transaction management

### 4. DTO Pattern
- Data transfer between layers
- API request/response objects

### 5. Singleton Pattern
- Spring beans
- Service instances

### 6. Factory Pattern
- Entity creation
- DTO conversion

### 7. Strategy Pattern
- Authentication strategies
- Ranking algorithms

### 8. Observer Pattern
- Event handling
- Notifications

## Key Features Implementation

### 1. Authentication & Authorization
```
JWT Token Generation → JwtUtil
User Authentication → CustomUserDetailsService
Security Configuration → SecurityConfig
Role-Based Access → @PreAuthorize annotations
```

### 2. Resume Analysis
```
PDF Upload → JobSeekerController
Text Extraction → AIService (PDFBox)
NLP Processing → Python AI Service (spaCy)
Skill Extraction → Pattern matching + NLP
ATS Scoring → Multi-factor algorithm
```

### 3. Job Matching
```
Skill Comparison → ApplicationService
Experience Matching → calculateExperienceMatch()
Score Calculation → calculateMatchScore()
Ranking → updateRankings()
```

### 4. Candidate Ranking
```
Match Score (70%) + Experience Score (30%)
Sorted by: matchScore DESC, atsScore DESC
Rank Position Assignment
Real-time Updates
```

## Database Relationships

### One-to-One
- User ↔ JobSeekerProfile
- User ↔ RecruiterProfile

### One-to-Many
- JobSeekerProfile → Education
- JobSeekerProfile → Experience
- JobSeekerProfile → Certification
- RecruiterProfile → Job
- Job → Application
- User → Application

### Many-to-Many
- Job ↔ Skill (via job_skills)

## API Endpoints Summary

### Public Endpoints
- POST /api/auth/register
- POST /api/auth/login

### Job Seeker Endpoints
- GET /api/jobs
- GET /api/jobs/{id}
- POST /api/jobseeker/apply/{jobId}
- GET /api/jobseeker/applications
- POST /api/jobseeker/resume/upload

### Recruiter Endpoints
- POST /api/recruiter/jobs
- GET /api/recruiter/jobs/{jobId}/candidates

### Admin Endpoints
- GET /api/admin/users
- DELETE /api/admin/users/{id}

## Security Features

1. **Password Encryption**: BCrypt
2. **JWT Authentication**: Stateless tokens
3. **CORS Configuration**: Cross-origin support
4. **Input Validation**: Bean Validation
5. **SQL Injection Prevention**: Parameterized queries
6. **XSS Protection**: Input sanitization
7. **HTTPS Support**: Production deployment

## Performance Optimizations

1. **Database Indexing**: Primary keys, foreign keys
2. **Pagination**: All list endpoints
3. **Lazy Loading**: JPA relationships
4. **Connection Pooling**: HikariCP
5. **Caching**: Future enhancement (Redis)
6. **Query Optimization**: JPA queries

## Testing Strategy

### Backend Tests
- Unit Tests: Service layer
- Integration Tests: Repository layer
- API Tests: Controller layer
- Security Tests: Authentication

### Frontend Tests
- Component Tests: React Testing Library
- Integration Tests: User flows
- E2E Tests: Cypress (future)

## Deployment Architecture

```
┌─────────────┐
│   Client    │
│  (Browser)  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   React     │
│  Frontend   │
│  (Port 3000)│
└──────┬──────┘
       │
       ▼
┌─────────────┐      ┌─────────────┐
│ Spring Boot │◄────►│   MySQL     │
│   Backend   │      │  Database   │
│  (Port 8080)│      │  (Port 3306)│
└──────┬──────┘      └─────────────┘
       │
       ▼
┌─────────────┐
│   Python    │
│ AI Service  │
│  (Port 5000)│
└─────────────┘
```

## Future Enhancements

1. **Microservices Architecture**
2. **Redis Caching**
3. **Kafka Event Streaming**
4. **Elasticsearch Job Search**
5. **AWS S3 Resume Storage**
6. **Email Notifications**
7. **Real-time Chat**
8. **Video Interviews**
9. **Advanced Analytics Dashboard**
10. **Mobile Application**
