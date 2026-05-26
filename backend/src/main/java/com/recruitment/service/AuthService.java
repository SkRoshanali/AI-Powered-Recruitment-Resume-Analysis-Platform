package com.recruitment.service;

import com.recruitment.dto.AuthRequest;
import com.recruitment.dto.AuthResponse;
import com.recruitment.dto.RegisterRequest;
import com.recruitment.entity.JobSeekerProfile;
import com.recruitment.entity.RecruiterProfile;
import com.recruitment.entity.User;
import com.recruitment.exception.ResourceAlreadyExistsException;
import com.recruitment.repository.JobSeekerProfileRepository;
import com.recruitment.repository.RecruiterProfileRepository;
import com.recruitment.repository.UserRepository;
import com.recruitment.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(User.Role.valueOf(request.getRole()));
        user.setIsActive(true);
        
        user = userRepository.save(user);
        
        // Create profile based on role
        if (user.getRole() == User.Role.JOB_SEEKER) {
            JobSeekerProfile profile = new JobSeekerProfile();
            profile.setUser(user);
            jobSeekerProfileRepository.save(profile);
        } else if (user.getRole() == User.Role.RECRUITER) {
            RecruiterProfile profile = new RecruiterProfile();
            profile.setUser(user);
            profile.setCompanyName(request.getCompanyName());
            profile.setCompanyWebsite(request.getCompanyWebsite());
            recruiterProfileRepository.save(profile);
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }
    
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }
}
