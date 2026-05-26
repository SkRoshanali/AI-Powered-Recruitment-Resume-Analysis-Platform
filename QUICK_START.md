# Quick Start Guide

## Prerequisites Check
```bash
# Check Java version (need 17+)
java -version

# Check Maven
mvn -version

# Check Node.js (need 18+)
node -v
npm -v

# Check Python (need 3.11+)
python --version

# Check MySQL
mysql --version

# Check Docker (optional)
docker --version
docker-compose --version
```

## Option 1: Docker Setup (Recommended)

### Step 1: Clone and Navigate
```bash
cd recruitment-platform
```

### Step 2: Start All Services
```bash
docker-compose up --build
```

### Step 3: Access Applications
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **AI Service**: http://localhost:5000

### Step 4: Test the System
1. Open http://localhost:3000
2. Click "Register"
3. Create a Job Seeker account
4. Browse jobs and apply

---

## Option 2: Manual Setup

### Step 1: Setup MySQL Database
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE recruitment_db;

# Exit MySQL
exit;
```

### Step 2: Start Backend
```bash
cd backend

# Update src/main/resources/application.yml with your MySQL credentials
# Default: username=root, password=root

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start on **http://localhost:8080**

### Step 3: Start AI Service
```bash
cd ai-service

# Create virtual environment
python -m venv venv

# Activate virtual environment
# On Windows:
venv\Scripts\activate
# On Mac/Linux:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Download spaCy model
python -m spacy download en_core_web_sm

# Run service
python app.py
```

AI Service will start on **http://localhost:5000**

### Step 4: Start Frontend
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will start on **http://localhost:3000**

---

## Testing the Application

### 1. Register Users

#### Job Seeker Registration
```json
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "jobseeker@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phone": "1234567890",
  "role": "JOB_SEEKER"
}
```

#### Recruiter Registration
```json
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "recruiter@example.com",
  "password": "password123",
  "fullName": "Jane Smith",
  "phone": "0987654321",
  "role": "RECRUITER",
  "companyName": "Tech Corp",
  "companyWebsite": "https://techcorp.com"
}
```

### 2. Login
```json
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "jobseeker@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "jobseeker@example.com",
  "fullName": "John Doe",
  "role": "JOB_SEEKER"
}
```

### 3. Create a Job (Recruiter)
```json
POST http://localhost:8080/api/recruiter/jobs
Authorization: Bearer <recruiter-token>
Content-Type: application/json

{
  "title": "Senior Java Developer",
  "description": "We are looking for an experienced Java developer with Spring Boot expertise.",
  "location": "New York, NY",
  "jobType": "FULL_TIME",
  "experienceLevel": "SENIOR_LEVEL",
  "salaryRange": "$120,000 - $150,000",
  "minExperience": 5,
  "maxExperience": 8,
  "requiredSkills": ["Java", "Spring Boot", "MySQL", "Docker", "AWS"]
}
```

### 4. Upload Resume (Job Seeker)
```bash
# Using curl
curl -X POST http://localhost:8080/api/jobseeker/resume/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@/path/to/resume.pdf"
```

### 5. Apply for Job
```json
POST http://localhost:8080/api/jobseeker/apply/1?coverLetter=I am interested in this position
Authorization: Bearer <jobseeker-token>
```

### 6. View Ranked Candidates (Recruiter)
```json
GET http://localhost:8080/api/recruiter/jobs/1/candidates
Authorization: Bearer <recruiter-token>
```

---

## Sample Test Data

### Create Sample Skills
The system automatically creates skills when jobs are posted. Common skills include:
- Java, Python, JavaScript
- Spring Boot, React, Angular
- MySQL, PostgreSQL, MongoDB
- Docker, Kubernetes, AWS
- Git, Jenkins, CI/CD

### Sample Resume Content
Create a PDF resume with content like:
```
John Doe
Software Engineer

EXPERIENCE
Senior Java Developer at Tech Corp (2020-Present)
- Developed microservices using Spring Boot
- Implemented REST APIs
- Worked with MySQL and Docker

EDUCATION
Bachelor of Computer Science
University of Technology (2016-2020)

SKILLS
Java, Spring Boot, MySQL, Docker, AWS, Git, REST API, Microservices

CERTIFICATIONS
AWS Certified Developer
Oracle Certified Java Programmer
```

---

## Troubleshooting

### Backend Won't Start
```bash
# Check if port 8080 is in use
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux

# Check MySQL connection
mysql -u root -p -e "SHOW DATABASES;"

# Check application.yml configuration
cat backend/src/main/resources/application.yml
```

### Frontend Won't Start
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Check if port 3000 is in use
netstat -ano | findstr :3000  # Windows
lsof -i :3000                 # Mac/Linux
```

### AI Service Issues
```bash
# Verify Python version
python --version

# Reinstall dependencies
pip install --upgrade -r requirements.txt

# Download spaCy model again
python -m spacy download en_core_web_sm

# Test AI service
curl http://localhost:5000/health
```

### Database Connection Failed
```bash
# Check MySQL is running
# Windows:
net start MySQL80

# Mac:
brew services start mysql

# Linux:
sudo systemctl start mysql

# Test connection
mysql -u root -p -e "SELECT 1;"
```

### Docker Issues
```bash
# Stop all containers
docker-compose down

# Remove volumes
docker-compose down -v

# Rebuild images
docker-compose build --no-cache

# Start fresh
docker-compose up --build
```

---

## Default Credentials

### MySQL
- **Host**: localhost
- **Port**: 3306
- **Database**: recruitment_db
- **Username**: root
- **Password**: root

### JWT Secret
- Located in: `backend/src/main/resources/application.yml`
- Key: `jwt.secret`
- **Change in production!**

---

## API Testing with Postman

### Import Collection
1. Open Postman
2. Click Import
3. Create new collection "Recruitment Platform"
4. Add requests from API_DOCUMENTATION.md

### Environment Variables
```
base_url: http://localhost:8080/api
token: <your-jwt-token>
```

---

## Verification Checklist

- [ ] MySQL database created
- [ ] Backend running on port 8080
- [ ] AI service running on port 5000
- [ ] Frontend running on port 3000
- [ ] Can register new user
- [ ] Can login successfully
- [ ] Can view jobs list
- [ ] Can upload resume
- [ ] Can apply for job
- [ ] Recruiter can post job
- [ ] Recruiter can view candidates
- [ ] Swagger UI accessible

---

## Next Steps

1. **Explore Swagger UI**: http://localhost:8080/swagger-ui.html
2. **Read API Documentation**: API_DOCUMENTATION.md
3. **Review Database Schema**: DATABASE_SCHEMA.md
4. **Check Project Structure**: PROJECT_STRUCTURE.md
5. **Deploy to Production**: DEPLOYMENT_GUIDE.md

---

## Support

For issues or questions:
1. Check troubleshooting section above
2. Review error logs in console
3. Verify all prerequisites are installed
4. Ensure all services are running
5. Check firewall/antivirus settings

---

## Success Indicators

✅ All services start without errors
✅ Can access frontend at localhost:3000
✅ Can access Swagger UI at localhost:8080/swagger-ui.html
✅ Can register and login users
✅ Can create and view jobs
✅ Can upload resume and get analysis
✅ Can apply for jobs and see match scores
✅ Recruiters can view ranked candidates

**Congratulations! Your AI-Powered Recruitment Platform is ready!** 🎉
