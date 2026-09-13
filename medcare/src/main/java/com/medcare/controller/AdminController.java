package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.Appointment;
import com.medcare.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final OrderRepository orderRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AdminController(UserRepository userRepository, MedicineRepository medicineRepository,
                           OrderRepository orderRepository, AppointmentRepository appointmentRepository,
                           DoctorRepository doctorRepository) {
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
        this.orderRepository = orderRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userRepository.count());
        data.put("totalMedicines", medicineRepository.count());
        data.put("totalOrders", orderRepository.count());
        data.put("totalAppointments", appointmentRepository.count());
        data.put("totalDoctors", doctorRepository.count());
        data.put("lowStockMedicines", medicineRepository.findByStockQuantityLessThan(15).size());
        data.put("pendingAppointments",
                appointmentRepository.findByStatus(Appointment.AppointmentStatus.PENDING).size());
        return ResponseEntity.ok(ApiResponse.ok("Stats fetched", data));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> allUsers() {
        List<UserDTO> list = userRepository.findAll().stream().map(UserDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Users fetched", list));
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<ApiResponse<UserDTO>> toggleUser(@PathVariable Long id) {
        return userRepository.findById(id).map(u -> {
            u.setEnabled(!u.isEnabled());
            return ResponseEntity.ok(ApiResponse.ok(
                    u.isEnabled() ? "User enabled" : "User suspended",
                    UserDTO.from(userRepository.save(u))));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted", null));
    }
}
