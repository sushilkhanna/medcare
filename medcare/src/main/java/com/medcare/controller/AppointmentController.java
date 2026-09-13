package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.*;
import com.medcare.repository.*;
import com.medcare.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository,
                                  UserRepository userRepository, JwtUtil jwtUtil) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private User getUser(String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userRepository.findByEmail(email).orElseThrow();
    }

    private AppointmentDTO toDTO(Appointment a) {
        return AppointmentDTO.builder()
                .id(a.getId())
                .patientId(a.getPatient().getId()).patientName(a.getPatient().getName())
                .doctorId(a.getDoctor().getId()).doctorName(a.getDoctor().getUser().getName())
                .specialization(a.getDoctor().getSpecialization())
                .appointmentDate(a.getAppointmentDate()).appointmentTime(a.getAppointmentTime())
                .reason(a.getReason()).notes(a.getNotes()).status(a.getStatus().name())
                .bookedAt(a.getBookedAt()).build();
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<AppointmentDTO>> book(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody AppointmentRequest req) {
        if (req.getAppointmentDate() != null && req.getAppointmentDate().isBefore(java.time.LocalDate.now())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Appointment date cannot be in the past"));
        }

        User patient = getUser(auth);
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Check doctor conflict
        List<Appointment> existing = appointmentRepository.findByDoctorAndAppointmentDateAndAppointmentTime(
                doctor, req.getAppointmentDate(), req.getAppointmentTime());
        boolean hasConflict = existing.stream().anyMatch(a ->
                a.getStatus() == Appointment.AppointmentStatus.PENDING ||
                a.getStatus() == Appointment.AppointmentStatus.CONFIRMED);
        if (hasConflict) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("This slot is already booked with Dr. " + doctor.getUser().getName() + ". Please pick another time."));
        }

        Appointment appt = Appointment.builder().patient(patient).doctor(doctor)
                .appointmentDate(req.getAppointmentDate())
                .appointmentTime(req.getAppointmentTime())
                .reason(req.getReason())
                .status(Appointment.AppointmentStatus.PENDING).build();
        return ResponseEntity.ok(ApiResponse.ok("Appointment booked successfully", toDTO(appointmentRepository.save(appt))));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> myAppointments(
            @RequestHeader("Authorization") String auth) {
        List<AppointmentDTO> list = appointmentRepository
                .findByPatientOrderByAppointmentDateDesc(getUser(auth))
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(ApiResponse.ok("Appointments fetched", list));
    }

    @GetMapping("/doctor/my")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> doctorAppointments(
            @RequestHeader("Authorization") String auth) {
        User user = getUser(auth);
        Doctor doctor = doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        List<AppointmentDTO> list = appointmentRepository
                .findByDoctorOrderByAppointmentDateAsc(doctor)
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(ApiResponse.ok("Doctor appointments", list));
    }

    // Doctors/Admins can only act on appointments; a DOCTOR must own the
    // appointment (be the assigned doctor) — otherwise any doctor could
    // confirm/reject/cancel any other doctor's patients, which is a data
    // integrity + privacy issue. ADMIN is allowed to act on any appointment.
    private boolean canManage(User actor, Appointment a) {
        if (actor.getRole() == User.Role.ADMIN) return true;
        return doctorRepository.findByUser(actor)
                .map(d -> d.getId().equals(a.getDoctor().getId()))
                .orElse(false);
    }

    @PatchMapping("/doctor/{id}/confirm")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentDTO>> confirm(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        User actor = getUser(auth);
        return appointmentRepository.findById(id).map(a -> {
            if (!canManage(actor, a))
                return ResponseEntity.status(403).body(ApiResponse.<AppointmentDTO>error("Not your appointment"));
            if (a.getStatus() != Appointment.AppointmentStatus.PENDING)
                return ResponseEntity.badRequest().body(ApiResponse.<AppointmentDTO>error("Only pending appointments can be confirmed"));
            a.setStatus(Appointment.AppointmentStatus.CONFIRMED);
            return ResponseEntity.ok(ApiResponse.ok("Confirmed", toDTO(appointmentRepository.save(a))));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/doctor/{id}/reject")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentDTO>> reject(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestParam(required = false) String reason) {
        User actor = getUser(auth);
        return appointmentRepository.findById(id).map(a -> {
            if (!canManage(actor, a))
                return ResponseEntity.status(403).body(ApiResponse.<AppointmentDTO>error("Not your appointment"));
            if (a.getStatus() != Appointment.AppointmentStatus.PENDING)
                return ResponseEntity.badRequest().body(ApiResponse.<AppointmentDTO>error("Only pending appointments can be rejected"));
            a.setStatus(Appointment.AppointmentStatus.REJECTED);
            if (reason != null) a.setNotes(reason);
            return ResponseEntity.ok(ApiResponse.ok("Rejected", toDTO(appointmentRepository.save(a))));
        }).orElse(ResponseEntity.notFound().build());
    }

    // NEW: a doctor can cancel an appointment (PENDING or CONFIRMED) that a
    // patient booked with them — e.g. the doctor becomes unavailable. This
    // was previously impossible: the only cancel endpoint checked patient
    // ownership, so doctors had no way to cancel.
    @PatchMapping("/doctor/{id}/cancel")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentDTO>> doctorCancel(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long id, @RequestParam(required = false) String reason) {
        User actor = getUser(auth);
        return appointmentRepository.findById(id).map(a -> {
            if (!canManage(actor, a))
                return ResponseEntity.status(403).body(ApiResponse.<AppointmentDTO>error("Not your appointment"));
            if (a.getStatus() != Appointment.AppointmentStatus.PENDING
                    && a.getStatus() != Appointment.AppointmentStatus.CONFIRMED)
                return ResponseEntity.badRequest().body(ApiResponse.<AppointmentDTO>error("This appointment can no longer be cancelled"));
            a.setStatus(Appointment.AppointmentStatus.CANCELLED);
            a.setNotes(reason != null ? reason : "Cancelled by doctor");
            return ResponseEntity.ok(ApiResponse.ok("Appointment cancelled", toDTO(appointmentRepository.save(a))));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<AppointmentDTO>> cancel(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        User user = getUser(auth);
        return appointmentRepository.findById(id)
                .filter(a -> a.getPatient().getId().equals(user.getId()))
                .map(a -> {
            if (a.getStatus() != Appointment.AppointmentStatus.PENDING
                    && a.getStatus() != Appointment.AppointmentStatus.CONFIRMED) {
                return ResponseEntity.badRequest().body(ApiResponse.<AppointmentDTO>error("This appointment can no longer be cancelled"));
            }
            a.setStatus(Appointment.AppointmentStatus.CANCELLED);
            return ResponseEntity.ok(ApiResponse.ok("Cancelled", toDTO(appointmentRepository.save(a))));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AppointmentDTO>>> allAppointments() {
        List<AppointmentDTO> list = appointmentRepository.findAll()
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(ApiResponse.ok("All appointments", list));
    }
}
