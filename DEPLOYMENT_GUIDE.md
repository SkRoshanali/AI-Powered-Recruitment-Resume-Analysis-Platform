# Deployment Guide

## Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+
- Python 3.11+
- MySQL 8.0+
- Docker & Docker Compose (optional)

## Local Development Setup

### 1. Database Setup
```bash
# Start MySQL
mysql -u root -p

# Create database
CREATE DATABASE recruitment_db;
```

### 2. Backend Setup
```bash
cd backend

# Update application.yml with your MySQL credentials
# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### 3. AI Service Setup
```bash
cd ai-service

# Create virtual environment
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt
python -m spacy download en_core_web_sm

# Run service
python app.py
```

AI Service will start on `http://localhost:5000`

### 4. Frontend Setup
```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```

Frontend will start on `http://localhost:3000`

## Docker Deployment

### Build and Run All Services
```bash
# Build and start all containers
docker-compose up --build

# Run in detached mode
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f
```

### Access Services
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- AI Service: http://localhost:5000
- Swagger UI: http://localhost:8080/swagger-ui.html

## Production Deployment

### AWS Deployment
1. **RDS MySQL**: Create MySQL instance
2. **EC2/ECS**: Deploy backend Spring Boot application
3. **S3 + CloudFront**: Host React frontend
4. **EC2**: Deploy Python AI service
5. **Application Load Balancer**: Route traffic

### Environment Variables
```bash
# Backend
SPRING_DATASOURCE_URL=jdbc:mysql://[host]:3306/recruitment_db
SPRING_DATASOURCE_USERNAME=[username]
SPRING_DATASOURCE_PASSWORD=[password]
JWT_SECRET=[your-secret-key]
AI_SERVICE_URL=http://[ai-service-host]:5000

# Frontend
REACT_APP_API_URL=https://[backend-url]/api
```

## Testing

### Backend Tests
```bash
cd backend
mvn test
```

### API Testing with Postman
Import the API collection from `docs/postman_collection.json`

## Monitoring
- Application logs: `logs/application.log`
- Database monitoring: MySQL Workbench
- API monitoring: Swagger UI

## Troubleshooting

### Common Issues

**Database Connection Failed**
- Verify MySQL is running
- Check credentials in application.yml
- Ensure database exists

**AI Service Not Responding**
- Check if Python service is running on port 5000
- Verify spaCy model is downloaded
- Check firewall settings

**Frontend Can't Connect to Backend**
- Verify backend is running on port 8080
- Check CORS configuration
- Verify API URL in frontend

## Security Checklist
- [ ] Change default JWT secret
- [ ] Use strong database passwords
- [ ] Enable HTTPS in production
- [ ] Configure firewall rules
- [ ] Set up rate limiting
- [ ] Enable SQL injection protection
- [ ] Implement input validation
