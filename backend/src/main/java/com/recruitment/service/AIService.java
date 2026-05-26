package com.recruitment.service;

import com.recruitment.dto.ResumeAnalysisResponse;
import com.recruitment.entity.Job;
import com.recruitment.entity.JobSeekerProfile;
import com.recruitment.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {
    
    @Value("${ai.service.url}")
    private String aiServiceUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final JobRepository jobRepository;
    
    public String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
    
    public ResumeAnalysisResponse analyzeResume(String resumeText) {
        try {
            // Call Python AI service
            Map<String, String> request = new HashMap<>();
            request.put("text", resumeText);
            
            ResumeAnalysisResponse response = restTemplate.postForObject(
                    aiServiceUrl + "/analyze",
                    request,
                    ResumeAnalysisResponse.class
            );
            
            return response;
        } catch (Exception e) {
            // Fallback to basic analysis
            return performBasicAnalysis(resumeText);
        }
    }
    
    private ResumeAnalysisResponse performBasicAnalysis(String text) {
        ResumeAnalysisResponse response = new ResumeAnalysisResponse();
        
        // Extract skills using keyword matching
        List<String> skills = extractSkills(text);
        response.setSkills(skills);
        
        // Calculate basic ATS score
        double atsScore = calculateATSScore(text, skills);
        response.setAtsScore(atsScore);
        
        response.setEducation(new ArrayList<>());
        response.setExperience(new ArrayList<>());
        response.setCertifications(new ArrayList<>());
        response.setMetadata(new HashMap<>());
        
        return response;
    }
    
    private List<String> extractSkills(String text) {
        String lowerText = text.toLowerCase();
        List<String> commonSkills = Arrays.asList(
                "java", "python", "javascript", "react", "angular", "vue",
                "spring boot", "hibernate", "sql", "mysql", "postgresql",
                "mongodb", "docker", "kubernetes", "aws", "azure", "gcp",
                "git", "jenkins", "ci/cd", "rest api", "microservices",
                "html", "css", "node.js", "express", "django", "flask",
                "machine learning", "data science", "ai", "nlp"
        );
        
        return commonSkills.stream()
                .filter(lowerText::contains)
                .collect(Collectors.toList());
    }
    
    private double calculateATSScore(String text, List<String> skills) {
        double score = 50.0;
        
        // Skill presence
        score += Math.min(skills.size() * 3, 30);
        
        // Length check
        int wordCount = text.split("\\s+").length;
        if (wordCount > 300 && wordCount < 2000) {
            score += 10;
        }
        
        // Section keywords
        String lowerText = text.toLowerCase();
        if (lowerText.contains("experience")) score += 5;
        if (lowerText.contains("education")) score += 5;
        
        return Math.min(score, 100.0);
    }
    
    public List<Job> recommendJobs(JobSeekerProfile profile) {
        List<String> skills = new ArrayList<>(profile.getSkills());
        if (skills.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Job> matchingJobs = jobRepository.findBySkills(skills);
        
        // Sort by relevance
        return matchingJobs.stream()
                .sorted((j1, j2) -> {
                    int match1 = calculateJobMatch(profile, j1);
                    int match2 = calculateJobMatch(profile, j2);
                    return Integer.compare(match2, match1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }
    
    private int calculateJobMatch(JobSeekerProfile profile, Job job) {
        Set<String> profileSkills = profile.getSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        Set<String> jobSkills = job.getRequiredSkills().stream()
                .map(skill -> skill.getName().toLowerCase())
                .collect(Collectors.toSet());
        
        Set<String> intersection = new HashSet<>(profileSkills);
        intersection.retainAll(jobSkills);
        
        return intersection.size();
    }
}
