package com.recruitment.repository;

import com.recruitment.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);
    
    Page<Job> findByRecruiterId(Long recruiterId, Pageable pageable);
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Job> searchJobs(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT j FROM Job j JOIN j.requiredSkills s WHERE s.name IN :skills AND j.status = 'ACTIVE'")
    List<Job> findBySkills(@Param("skills") List<String> skills);
}
