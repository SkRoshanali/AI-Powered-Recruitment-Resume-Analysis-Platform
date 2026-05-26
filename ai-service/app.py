from flask import Flask, request, jsonify
from flask_cors import CORS
import spacy
import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)
CORS(app)

# Load spaCy model
try:
    nlp = spacy.load("en_core_web_sm")
except:
    print("Downloading spaCy model...")
    import os
    os.system("python -m spacy download en_core_web_sm")
    nlp = spacy.load("en_core_web_sm")

# Common technical skills database
TECH_SKILLS = {
    'programming': ['java', 'python', 'javascript', 'c++', 'c#', 'ruby', 'go', 'rust', 'php', 'swift', 'kotlin'],
    'web': ['react', 'angular', 'vue', 'html', 'css', 'node.js', 'express', 'django', 'flask', 'spring boot'],
    'database': ['sql', 'mysql', 'postgresql', 'mongodb', 'redis', 'oracle', 'cassandra', 'dynamodb'],
    'cloud': ['aws', 'azure', 'gcp', 'docker', 'kubernetes', 'terraform', 'jenkins', 'ci/cd'],
    'data': ['machine learning', 'deep learning', 'nlp', 'data science', 'tensorflow', 'pytorch', 'pandas', 'numpy'],
    'tools': ['git', 'jira', 'agile', 'scrum', 'rest api', 'graphql', 'microservices']
}

@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'healthy'}), 200

@app.route('/analyze', methods=['POST'])
def analyze_resume():
    data = request.json
    text = data.get('text', '')
    
    if not text:
        return jsonify({'error': 'No text provided'}), 400
    
    # Process text with spaCy
    doc = nlp(text.lower())
    
    # Extract skills
    skills = extract_skills(text)
    
    # Extract education
    education = extract_education(text)
    
    # Extract experience
    experience = extract_experience(text)
    
    # Extract certifications
    certifications = extract_certifications(text)
    
    # Calculate ATS score
    ats_score = calculate_ats_score(text, skills, education, experience)
    
    # Generate summary
    summary = generate_summary(doc)
    
    response = {
        'skills': skills,
        'education': education,
        'experience': experience,
        'certifications': certifications,
        'atsScore': ats_score,
        'summary': summary,
        'metadata': {
            'wordCount': len(text.split()),
            'skillCount': len(skills)
        }
    }
    
    return jsonify(response), 200

def extract_skills(text):
    text_lower = text.lower()
    found_skills = []
    
    for category, skills in TECH_SKILLS.items():
        for skill in skills:
            if skill in text_lower:
                found_skills.append(skill)
    
    # Remove duplicates
    return list(set(found_skills))

def extract_education(text):
    education = []
    education_keywords = ['bachelor', 'master', 'phd', 'degree', 'university', 'college', 'b.tech', 'm.tech', 'mba']
    
    lines = text.split('\n')
    for line in lines:
        line_lower = line.lower()
        if any(keyword in line_lower for keyword in education_keywords):
            education.append(line.strip())
    
    return education[:5]  # Limit to 5 entries

def extract_experience(text):
    experience = []
    experience_keywords = ['experience', 'worked', 'developer', 'engineer', 'manager', 'analyst']
    
    lines = text.split('\n')
    for line in lines:
        line_lower = line.lower()
        if any(keyword in line_lower for keyword in experience_keywords):
            if len(line.strip()) > 20:  # Filter out short lines
                experience.append(line.strip())
    
    return experience[:10]  # Limit to 10 entries

def extract_certifications(text):
    certifications = []
    cert_keywords = ['certified', 'certification', 'certificate', 'aws certified', 'azure certified', 'oracle certified']
    
    lines = text.split('\n')
    for line in lines:
        line_lower = line.lower()
        if any(keyword in line_lower for keyword in cert_keywords):
            certifications.append(line.strip())
    
    return certifications[:5]

def calculate_ats_score(text, skills, education, experience):
    score = 50.0  # Base score
    
    # Skills contribution (30 points)
    skill_score = min(len(skills) * 2, 30)
    score += skill_score
    
    # Education contribution (10 points)
    if education:
        score += 10
    
    # Experience contribution (10 points)
    if experience:
        score += 10
    
    # Word count check
    word_count = len(text.split())
    if 300 < word_count < 2000:
        score += 5
    
    # Section structure
    text_lower = text.lower()
    if 'experience' in text_lower:
        score += 2
    if 'education' in text_lower:
        score += 2
    if 'skills' in text_lower:
        score += 2
    
    return min(score, 100.0)

def generate_summary(doc):
    sentences = [sent.text.strip() for sent in doc.sents]
    if len(sentences) > 3:
        return ' '.join(sentences[:3])
    return ' '.join(sentences)

@app.route('/match', methods=['POST'])
def match_resume_job():
    data = request.json
    resume_text = data.get('resumeText', '')
    job_description = data.get('jobDescription', '')
    
    if not resume_text or not job_description:
        return jsonify({'error': 'Missing text'}), 400
    
    # Calculate similarity
    vectorizer = TfidfVectorizer()
    tfidf_matrix = vectorizer.fit_transform([resume_text, job_description])
    similarity = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:2])[0][0]
    
    match_score = similarity * 100
    
    return jsonify({
        'matchScore': round(match_score, 2),
        'similarity': round(similarity, 4)
    }), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
