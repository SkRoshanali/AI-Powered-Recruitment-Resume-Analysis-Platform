package com.recruitment.controller;

import com.recruitment.dto.JobDTO;
import com.recruitment.service.JobService;
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

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Jobs", description = "Job Management APIs")
public class JobController {
    
    private final JobService jobService;
    
    @GetMapping
    @Operation(summary = "Get all active jobs")
    public ResponseEntity<Page<JobDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.getAllJobs(pageable));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search jobs by keyword")
    public ResponseEntity<Page<JobDTO>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(jobService.searchJobs(keyword, pageable));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }
}
