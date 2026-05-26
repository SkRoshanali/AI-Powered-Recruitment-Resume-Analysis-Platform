package com.recruitment.controller;

import com.recruitment.dto.ApplicationDTO;
import com.recruitment.dto.ResumeAnalysisResponse;
import com.recruitment.entity.JobSeekerProfile;
import com.recruitment.entity.User;
import com.recruitment.repository.JobSeekerProfileRepository;
import com.recruitment.repository.UserRepository;
import com.recruitment.service.AIService;
import com.recruitment.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;

@RestController
@RequestMapping("/api/jobseeker")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Job Seeker", description = "Job Seeker APIs")
public class JobSeekerController {
    
    private final ApplicationService applicationService;
    private final AIService aiService;
    private final UserRepository userRepository;
    private final JobSeekerProfileRepository profileRepository;
    
    @PostMapping("/apply/{jobId}")
    @Operation(summary = "Apply for a job")
    public ResponseEntity<ApplicationDTO> applyForJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) String coverLetter,
            Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(applicationService.applyForJob(jobId, user.getId(), coverLetter));
    }
    
    @GetMapping("/applications")
    @Operation(summary = "Get user applications")
    public ResponseEntity<Page<ApplicationDTO>> getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(applicationService.getUserApplications(user.getId(), pageable));
    }
    
    @PostMapping("/resume/upload")
    @Operation(summary = "Upload and analyze resume")
    public ResponseEntity<ResumeAnalysisResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {
        
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        JobSeekerProfile profile = profileRepository.findByUserId(user.getId()).orElseThrow();
        
        // Extract text from PDF
        String resumeText = aiService.extractTextFromPDF(file);
        
        // Analyze resume
        ResumeAnalysisResponse analysis = aiService.analyzeResume(resumeText);
        
        // Update profile
        profile.setSkills(new HashSet<>(analysis.getSkills()));
        profile.setAtsScore(analysis.getAtsScore());
        profileRepository.save(profile);
        
        return ResponseEntity.ok(analysis);
    }
}
