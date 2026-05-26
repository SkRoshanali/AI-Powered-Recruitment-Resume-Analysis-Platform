package com.recruitment.service;

import com.recruitment.dto.JobDTO;
import com.recruitment.entity.Job;
import com.recruitment.entity.RecruiterProfile;
import com.recruitment.entity.Skill;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.repository.JobRepository;
import com.recruitment.repository.RecruiterProfileRepository;
import com.recruitment.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    
    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final SkillRepository skillRepository;
    
    @Transactional
    public JobDTO createJob(JobDTO jobDTO, Long userId) {
        RecruiterProfile recruiter = recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter profile not found"));
        
        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setDescription(jobDTO.getDescription());
        job.setLocation(jobDTO.getLocation());
        job.setJobType(Job.JobType.valueOf(jobDTO.getJobType()));
        job.setExperienceLevel(Job.ExperienceLevel.valueOf(jobDTO.getExperienceLevel()));
        job.setSalaryRange(jobDTO.getSalaryRange());
        job.setMinExperience(jobDTO.getMinExperience());
        job.setMaxExperience(jobDTO.getMaxExperience());
        job.setRecruiter(recruiter);
        job.setClosingDate(jobDTO.getClosingDate());
        
        // Handle skills
        Set<Skill> skills = new HashSet<>();
        if (jobDTO.getRequiredSkills() != null) {
            for (String skillName : jobDTO.getRequiredSkills()) {
                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill();
                            newSkill.setName(skillName);
                            return skillRepository.save(newSkill);
                        });
                skills.add(skill);
            }
        }
        job.setRequiredSkills(skills);
        
        job = jobRepository.save(job);
        return convertToDTO(job);
    }
    
    public Page<JobDTO> getAllJobs(Pageable pageable) {
        return jobRepository.findByStatus(Job.JobStatus.ACTIVE, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<JobDTO> searchJobs(String keyword, Pageable pageable) {
        return jobRepository.searchJobs(keyword, pageable)
                .map(this::convertToDTO);
    }
    
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return convertToDTO(job);
    }
    
    private JobDTO convertToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setJobType(job.getJobType().name());
        dto.setExperienceLevel(job.getExperienceLevel().name());
        dto.setSalaryRange(job.getSalaryRange());
        dto.setMinExperience(job.getMinExperience());
        dto.setMaxExperience(job.getMaxExperience());
        dto.setRequiredSkills(job.getRequiredSkills().stream()
                .map(Skill::getName)
                .collect(Collectors.toSet()));
        dto.setCompanyName(job.getRecruiter().getCompanyName());
        dto.setRecruiterId(job.getRecruiter().getId());
        dto.setStatus(job.getStatus().name());
        dto.setPostedAt(job.getPostedAt());
        dto.setClosingDate(job.getClosingDate());
        return dto;
    }
}
