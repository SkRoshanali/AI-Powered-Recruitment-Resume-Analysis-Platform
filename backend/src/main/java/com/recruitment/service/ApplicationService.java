package com.recruitment.service;

import com.recruitment.dto.ApplicationDTO;
import com.recruitment.entity.Application;
import com.recruitment.entity.Job;
import com.recruitment.entity.JobSeekerProfile;
import com.recruitment.entity.User;
import com.recruitment.exception.ResourceAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.repository.ApplicationRepository;
import com.recruitment.repository.JobRepository;
import com.recruitment.repository.JobSeekerProfileRepository;
import com.recruitment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobSeekerProfileRepository profileRepository;
    private final AIService aiService;
    
    @Transactional
    public ApplicationDTO applyForJob(Long jobId, Long userId, String coverLetter) {
        if (applicationRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new ResourceAlreadyExistsException("Already applied for this job");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        
        JobSeekerProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        
        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setCoverLetter(coverLetter);
        application.setStatus(Application.ApplicationStatus.APPLIED);
        
        // Calculate match score
        double matchScore = calculateMatchScore(profile, job);
        application.setMatchScore(matchScore);
        application.setAtsScore(profile.getAtsScore());
        
        // Identify missing skills
        String missingSkills = identifyMissingSkills(profile, job);
        application.setMissingSkills(missingSkills);
        
        application = applicationRepository.save(application);
        
        // Update rankings for this job
        updateRankings(jobId);
        
        return convertToDTO(application);
    }
    
    public Page<ApplicationDTO> getUserApplications(Long userId, Pageable pageable) {
        return applicationRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }
    
    public List<ApplicationDTO> getRankedCandidates(Long jobId) {
        List<Application> applications = applicationRepository.findRankedCandidatesByJobId(jobId);
        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void updateRankings(Long jobId) {
        List<Application> applications = applicationRepository.findRankedCandidatesByJobId(jobId);
        for (int i = 0; i < applications.size(); i++) {
            applications.get(i).setRankPosition(i + 1);
        }
        applicationRepository.saveAll(applications);
    }
    
    private double calculateMatchScore(JobSeekerProfile profile, Job job) {
        Set<String> candidateSkills = profile.getSkills();
        Set<String> jobSkills = job.getRequiredSkills().stream()
                .map(skill -> skill.getName().toLowerCase())
                .collect(Collectors.toSet());
        
        if (jobSkills.isEmpty()) return 0.0;
        
        Set<String> matchingSkills = new HashSet<>(candidateSkills);
        matchingSkills.retainAll(jobSkills);
        
        double skillMatch = (double) matchingSkills.size() / jobSkills.size();
        double experienceMatch = calculateExperienceMatch(profile, job);
        
        return (skillMatch * 0.7 + experienceMatch * 0.3) * 100;
    }
    
    private double calculateExperienceMatch(JobSeekerProfile profile, Job job) {
        if (profile.getExperienceYears() == null || job.getMinExperience() == null) {
            return 0.5;
        }
        
        int experience = profile.getExperienceYears();
        int minExp = job.getMinExperience();
        int maxExp = job.getMaxExperience() != null ? job.getMaxExperience() : minExp + 5;
        
        if (experience >= minExp && experience <= maxExp) {
            return 1.0;
        } else if (experience < minExp) {
            return Math.max(0, 1.0 - (minExp - experience) * 0.2);
        } else {
            return Math.max(0, 1.0 - (experience - maxExp) * 0.1);
        }
    }
    
    private String identifyMissingSkills(JobSeekerProfile profile, Job job) {
        Set<String> candidateSkills = profile.getSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        Set<String> jobSkills = job.getRequiredSkills().stream()
                .map(skill -> skill.getName().toLowerCase())
                .collect(Collectors.toSet());
        
        jobSkills.removeAll(candidateSkills);
        
        return String.join(", ", jobSkills);
    }
    
    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setUserId(application.getUser().getId());
        dto.setApplicantName(application.getUser().getFullName());
        dto.setApplicantEmail(application.getUser().getEmail());
        dto.setJobId(application.getJob().getId());
        dto.setJobTitle(application.getJob().getTitle());
        dto.setStatus(application.getStatus().name());
        dto.setCoverLetter(application.getCoverLetter());
        dto.setMatchScore(application.getMatchScore());
        dto.setAtsScore(application.getAtsScore());
        dto.setRankPosition(application.getRankPosition());
        dto.setMissingSkills(application.getMissingSkills());
        dto.setAppliedAt(application.getAppliedAt());
        return dto;
    }
}
