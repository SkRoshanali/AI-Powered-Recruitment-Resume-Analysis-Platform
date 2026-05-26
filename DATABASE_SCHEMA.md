# Database Schema

## Entity Relationship Diagram

```
┌─────────────┐         ┌──────────────────────┐
│    users    │────1:1──│ job_seeker_profiles  │
└─────────────┘         └──────────────────────┘
      │                           │
      │                           │ 1:M
      │                           ▼
      │                  ┌─────────────────┐
      │                  │   educations    │
      │                  └─────────────────┘
      │                  ┌─────────────────┐
      │                  │  experiences    │
      │                  └─────────────────┘
      │                  ┌─────────────────┐
      │                  │ certifications  │
      │                  └─────────────────┘
      │
      │ 1:1
      ▼
┌──────────────────────┐
│ recruiter_profiles   │
└──────────────────────┘
      │
      │ 1:M
      ▼
┌─────────────┐
│    jobs     │◄────M:M────┐
└─────────────┘            │
      │                    │
      │ 1:M          ┌─────────────┐
      ▼              │   skills    │
┌─────────────┐      └─────────────┘
│applications │
└─────────────┘
```

## Tables

### users
Primary user authentication and profile table.

| Column      | Type         | Constraints           | Description                    |
|-------------|--------------|----------------------|--------------------------------|
| id          | BIGINT       | PRIMARY KEY, AUTO    | Unique user identifier         |
| email       | VARCHAR(255) | UNIQUE, NOT NULL     | User email (login)             |
| password    | VARCHAR(255) | NOT NULL             | BCrypt hashed password         |
| full_name   | VARCHAR(255) | NOT NULL             | User's full name               |
| phone       | VARCHAR(20)  |                      | Contact phone number           |
| role        | ENUM         | NOT NULL             | JOB_SEEKER, RECRUITER, ADMIN   |
| is_active   | BOOLEAN      | DEFAULT TRUE         | Account status                 |
| created_at  | TIMESTAMP    | NOT NULL             | Account creation timestamp     |
| updated_at  | TIMESTAMP    | NOT NULL             | Last update timestamp          |

**Indexes:**
- PRIMARY KEY (id)
- UNIQUE INDEX (email)

---

### job_seeker_profiles
Extended profile for job seekers.

| Column           | Type         | Constraints           | Description                    |
|------------------|--------------|----------------------|--------------------------------|
| id               | BIGINT       | PRIMARY KEY, AUTO    | Profile identifier             |
| user_id          | BIGINT       | FOREIGN KEY, UNIQUE  | Reference to users.id          |
| summary          | TEXT         |                      | Professional summary           |
| location         | VARCHAR(255) |                      | Current location               |
| experience_years | INT          |                      | Total years of experience      |
| current_job_title| VARCHAR(255) |                      | Current position               |
| resume_path      | VARCHAR(500) |                      | Path to uploaded resume        |
| ats_score        | DOUBLE       |                      | Resume ATS compatibility score |

**Relationships:**
- ONE-TO-ONE with users
- ONE-TO-MANY with educations
- ONE-TO-MANY with experiences
- ONE-TO-MANY with certifications

---

### job_seeker_skills
Skills associated with job seeker profiles.

| Column     | Type         | Constraints           | Description                    |
|------------|--------------|----------------------|--------------------------------|
| profile_id | BIGINT       | FOREIGN KEY          | Reference to job_seeker_profiles.id |
| skill      | VARCHAR(100) | NOT NULL             | Skill name                     |

**Indexes:**
- INDEX (profile_id)

---

### educations
Educational background of job seekers.

| Column         | Type         | Constraints           | Description                    |
|----------------|--------------|----------------------|--------------------------------|
| id             | BIGINT       | PRIMARY KEY, AUTO    | Education record identifier    |
| profile_id     | BIGINT       | FOREIGN KEY          | Reference to job_seeker_profiles.id |
| institution    | VARCHAR(255) | NOT NULL             | University/College name        |
| degree         | VARCHAR(100) | NOT NULL             | Degree type                    |
| field_of_study | VARCHAR(100) |                      | Major/specialization           |
| start_date     | DATE         |                      | Start date                     |
| end_date       | DATE         |                      | End date                       |
| gpa            | DOUBLE       |                      | Grade point average            |
| description    | TEXT         |                      | Additional details             |

---

### experiences
Work experience of job seekers.

| Column            | Type         | Constraints           | Description                    |
|-------------------|--------------|----------------------|--------------------------------|
| id                | BIGINT       | PRIMARY KEY, AUTO    | Experience record identifier   |
| profile_id        | BIGINT       | FOREIGN KEY          | Reference to job_seeker_profiles.id |
| company           | VARCHAR(255) | NOT NULL             | Company name                   |
| job_title         | VARCHAR(255) | NOT NULL             | Position title                 |
| location          | VARCHAR(255) |                      | Work location                  |
| start_date        | DATE         |                      | Start date                     |
| end_date          | DATE         |                      | End date                       |
| currently_working | BOOLEAN      | DEFAULT FALSE        | Still working here             |
| description       | TEXT         |                      | Job responsibilities           |

---

### certifications
Professional certifications of job seekers.

| Column               | Type         | Constraints           | Description                    |
|----------------------|--------------|----------------------|--------------------------------|
| id                   | BIGINT       | PRIMARY KEY, AUTO    | Certification identifier       |
| profile_id           | BIGINT       | FOREIGN KEY          | Reference to job_seeker_profiles.id |
| name                 | VARCHAR(255) | NOT NULL             | Certification name             |
| issuing_organization | VARCHAR(255) |                      | Issuing body                   |
| issue_date           | DATE         |                      | Date issued                    |
| expiry_date          | DATE         |                      | Expiration date                |
| credential_id        | VARCHAR(100) |                      | Credential ID                  |
| credential_url       | VARCHAR(500) |                      | Verification URL               |

---

### recruiter_profiles
Extended profile for recruiters.

| Column              | Type         | Constraints           | Description                    |
|---------------------|--------------|----------------------|--------------------------------|
| id                  | BIGINT       | PRIMARY KEY, AUTO    | Profile identifier             |
| user_id             | BIGINT       | FOREIGN KEY, UNIQUE  | Reference to users.id          |
| company_name        | VARCHAR(255) | NOT NULL             | Company name                   |
| company_website     | VARCHAR(255) |                      | Company website URL            |
| company_description | TEXT         |                      | About the company              |
| industry            | VARCHAR(100) |                      | Industry sector                |
| company_size        | VARCHAR(50)  |                      | Number of employees            |
| location            | VARCHAR(255) |                      | Company location               |
| is_verified         | BOOLEAN      | DEFAULT FALSE        | Verification status            |

**Relationships:**
- ONE-TO-ONE with users
- ONE-TO-MANY with jobs

---

### jobs
Job postings created by recruiters.

| Column           | Type         | Constraints           | Description                    |
|------------------|--------------|----------------------|--------------------------------|
| id               | BIGINT       | PRIMARY KEY, AUTO    | Job identifier                 |
| title            | VARCHAR(255) | NOT NULL             | Job title                      |
| description      | TEXT         | NOT NULL             | Job description                |
| location         | VARCHAR(255) | NOT NULL             | Job location                   |
| job_type         | ENUM         |                      | FULL_TIME, PART_TIME, etc.     |
| experience_level | ENUM         |                      | ENTRY_LEVEL, MID_LEVEL, etc.   |
| salary_range     | VARCHAR(100) |                      | Salary information             |
| min_experience   | INT          |                      | Minimum years required         |
| max_experience   | INT          |                      | Maximum years required         |
| recruiter_id     | BIGINT       | FOREIGN KEY          | Reference to recruiter_profiles.id |
| status           | ENUM         | DEFAULT ACTIVE       | ACTIVE, CLOSED, DRAFT          |
| posted_at        | TIMESTAMP    | NOT NULL             | Job posting timestamp          |
| updated_at       | TIMESTAMP    | NOT NULL             | Last update timestamp          |
| closing_date     | TIMESTAMP    |                      | Application deadline           |

**Relationships:**
- MANY-TO-ONE with recruiter_profiles
- MANY-TO-MANY with skills
- ONE-TO-MANY with applications

**Indexes:**
- PRIMARY KEY (id)
- INDEX (recruiter_id)
- INDEX (status)

---

### skills
Master table of skills.

| Column   | Type         | Constraints           | Description                    |
|----------|--------------|----------------------|--------------------------------|
| id       | BIGINT       | PRIMARY KEY, AUTO    | Skill identifier               |
| name     | VARCHAR(100) | UNIQUE, NOT NULL     | Skill name                     |
| category | VARCHAR(50)  |                      | Skill category                 |

**Relationships:**
- MANY-TO-MANY with jobs

---

### job_skills
Junction table for jobs and skills (Many-to-Many).

| Column   | Type   | Constraints           | Description                    |
|----------|--------|----------------------|--------------------------------|
| job_id   | BIGINT | FOREIGN KEY          | Reference to jobs.id           |
| skill_id | BIGINT | FOREIGN KEY          | Reference to skills.id         |

**Indexes:**
- PRIMARY KEY (job_id, skill_id)
- INDEX (job_id)
- INDEX (skill_id)

---

### applications
Job applications submitted by job seekers.

| Column         | Type         | Constraints           | Description                    |
|----------------|--------------|----------------------|--------------------------------|
| id             | BIGINT       | PRIMARY KEY, AUTO    | Application identifier         |
| user_id        | BIGINT       | FOREIGN KEY          | Reference to users.id          |
| job_id         | BIGINT       | FOREIGN KEY          | Reference to jobs.id           |
| status         | ENUM         | DEFAULT APPLIED      | Application status             |
| cover_letter   | TEXT         |                      | Cover letter content           |
| match_score    | DOUBLE       |                      | Candidate-job match percentage |
| ats_score      | DOUBLE       |                      | Resume ATS score               |
| rank_position  | INT          |                      | Ranking among applicants       |
| missing_skills | TEXT         |                      | Skills gap analysis            |
| applied_at     | TIMESTAMP    | NOT NULL             | Application timestamp          |
| updated_at     | TIMESTAMP    | NOT NULL             | Last update timestamp          |

**Status Values:**
- APPLIED
- UNDER_REVIEW
- SHORTLISTED
- INTERVIEW_SCHEDULED
- REJECTED
- ACCEPTED

**Relationships:**
- MANY-TO-ONE with users
- MANY-TO-ONE with jobs

**Indexes:**
- PRIMARY KEY (id)
- INDEX (user_id)
- INDEX (job_id)
- UNIQUE INDEX (user_id, job_id)

---

## Database Normalization

The schema follows **Third Normal Form (3NF)**:

1. **1NF**: All tables have atomic values and unique rows
2. **2NF**: No partial dependencies (all non-key attributes depend on entire primary key)
3. **3NF**: No transitive dependencies (non-key attributes don't depend on other non-key attributes)

## Relationships Summary

### One-to-One (1:1)
- users ↔ job_seeker_profiles
- users ↔ recruiter_profiles

### One-to-Many (1:M)
- job_seeker_profiles → educations
- job_seeker_profiles → experiences
- job_seeker_profiles → certifications
- recruiter_profiles → jobs
- jobs → applications
- users → applications

### Many-to-Many (M:M)
- jobs ↔ skills (via job_skills junction table)

## Sample Queries

### Get all applications for a job with candidate details
```sql
SELECT 
    a.id, a.match_score, a.ats_score, a.rank_position,
    u.full_name, u.email,
    a.status, a.applied_at
FROM applications a
JOIN users u ON a.user_id = u.id
WHERE a.job_id = ?
ORDER BY a.match_score DESC, a.ats_score DESC;
```

### Get job seeker profile with all details
```sql
SELECT 
    jsp.*,
    GROUP_CONCAT(DISTINCT jss.skill) as skills,
    e.institution, e.degree,
    ex.company, ex.job_title
FROM job_seeker_profiles jsp
LEFT JOIN job_seeker_skills jss ON jsp.id = jss.profile_id
LEFT JOIN educations e ON jsp.id = e.profile_id
LEFT JOIN experiences ex ON jsp.id = ex.profile_id
WHERE jsp.user_id = ?
GROUP BY jsp.id;
```

### Find jobs matching candidate skills
```sql
SELECT DISTINCT j.*
FROM jobs j
JOIN job_skills js ON j.id = js.job_id
JOIN skills s ON js.skill_id = s.id
WHERE s.name IN (
    SELECT skill 
    FROM job_seeker_skills 
    WHERE profile_id = ?
)
AND j.status = 'ACTIVE';
```
