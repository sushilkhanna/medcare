package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.*;
import com.medcare.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorController(DoctorRepository doctorRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> listAll() {
        List<DoctorDTO> list = doctorRepository.findAll().stream().map(DoctorDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Doctors fetched", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorDTO>> getById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(d -> ResponseEntity.ok(ApiResponse.ok("Doctor found", DoctorDTO.from(d))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorDTO>> addDoctor(@RequestBody DoctorProfileRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = Doctor.builder().user(user)
                .specialization(req.getSpecialization()).qualification(req.getQualification())
                .experienceYears(req.getExperienceYears()).availability(req.getAvailability())
                .consultationFee(req.getConsultationFee()).bio(req.getBio())
                .rating(0.0).totalPatients(0).build();
        return ResponseEntity.ok(ApiResponse.ok("Doctor profile created", DoctorDTO.from(doctorRepository.save(doctor))));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> doctorUsers() {
        List<UserDTO> list = userRepository.findByRole(User.Role.DOCTOR)
                .stream().map(UserDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Doctor users", list));
    }

    public static class DoctorProfileRequest {
        private Long userId; private String specialization; private String qualification;
        private String availability; private String consultationFee; private String bio;
        private Integer experienceYears;
        public DoctorProfileRequest() {}
        public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
        public String getSpecialization() { return specialization; } public void setSpecialization(String v) { this.specialization = v; }
        public String getQualification() { return qualification; } public void setQualification(String v) { this.qualification = v; }
        public String getAvailability() { return availability; } public void setAvailability(String v) { this.availability = v; }
        public String getConsultationFee() { return consultationFee; } public void setConsultationFee(String v) { this.consultationFee = v; }
        public String getBio() { return bio; } public void setBio(String v) { this.bio = v; }
        public Integer getExperienceYears() { return experienceYears; } public void setExperienceYears(Integer v) { this.experienceYears = v; }
    }
}
