package com.recruitment.controller;

import com.recruitment.dto.ApplicationDTO;
import com.recruitment.dto.JobDTO;
import com.recruitment.entity.User;
import com.recruitment.repository.UserRepository;
import com.recruitment.service.ApplicationService;
import com.recruitment.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Recruiter", description = "Recruiter APIs")
public class RecruiterController {
    
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserRepository userRepository;
    
    @PostMapping("/jobs")
    @Operation(summary = "Create new job posting")
    public ResponseEntity<JobDTO> createJob(@RequestBody JobDTO jobDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        return ResponseEntity.ok(jobService.createJob(jobDTO, user.getId()));
    }
    
    @GetMapping("/jobs/{jobId}/candidates")
    @Operation(summary = "Get ranked candidates for a job")
    public ResponseEntity<List<ApplicationDTO>> getRankedCandidates(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getRankedCandidates(jobId));
    }
}
