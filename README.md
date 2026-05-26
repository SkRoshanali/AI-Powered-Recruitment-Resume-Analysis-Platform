# AI-Powered Recruitment & Resume Analysis Platform

## Overview
Enterprise-level intelligent hiring system with AI-based resume parsing, ATS scoring, and smart candidate-job matching.

## Tech Stack
- **Backend**: Java 17, Spring Boot, Hibernate, Spring Security, JWT
- **Frontend**: React.js, Bootstrap
- **Database**: MySQL
- **AI/NLP**: Python, spaCy, NLTK, Scikit-learn
- **Tools**: Maven, Docker, Swagger

## Features
- AI Resume Parsing & Skill Extraction
- ATS Compatibility Scoring
- Intelligent Job Recommendations
- Candidate Ranking System
- Skill Gap Analysis
- JWT Authentication & RBAC
- Real-time Notifications

## Project Structure
```
recruitment-platform/
├── backend/              # Spring Boot Application
├── frontend/             # React Application
├── ai-service/           # Python NLP Service
└── docker-compose.yml    # Container Orchestration
```

## Quick Start
```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend
cd frontend
npm install && npm start

# AI Service
cd ai-service
pip install -r requirements.txt
python app.py
```

## API Documentation
Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Roles
1. **Job Seeker** - Apply, upload resume, get recommendations
2. **Recruiter** - Post jobs, view ranked candidates
3. **Admin** - Manage users, verify jobs, monitor system
