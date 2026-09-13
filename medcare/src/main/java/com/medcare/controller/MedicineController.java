package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.Medicine;
import com.medcare.repository.MedicineRepository;
import com.medcare.service.RealtimeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineRepository medicineRepository;
    private final RealtimeService realtimeService;

    public MedicineController(MedicineRepository medicineRepository, RealtimeService realtimeService) {
        this.medicineRepository = medicineRepository;
        this.realtimeService = realtimeService;
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<MedicineDTO>>> listAll() {
        List<MedicineDTO> list = medicineRepository.findByAvailableTrue()
                .stream().map(MedicineDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Medicines fetched", list));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicineDTO>>> search(
            @RequestParam(required = false, defaultValue = "") String q) {
        if (q == null || q.trim().isEmpty()) {
            return listAll();
        }
        List<MedicineDTO> list = medicineRepository.findByNameContainingIgnoreCase(q.trim())
                .stream().map(MedicineDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Search results", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicineDTO>> getById(@PathVariable Long id) {
        return medicineRepository.findById(id)
                .map(m -> ResponseEntity.ok(ApiResponse.ok("Found", MedicineDTO.from(m))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineDTO>> add(@Valid @RequestBody MedicineRequest req) {
        Medicine m = Medicine.builder()
                .name(req.getName()).brand(req.getBrand())
                .category(req.getCategory()).description(req.getDescription())
                .price(req.getPrice()).stockQuantity(req.getStockQuantity())
                .requiresPrescription(req.isRequiresPrescription())
                .available(true).build();
        MedicineDTO dto = MedicineDTO.from(medicineRepository.save(m));
        realtimeService.medicineAdded(dto);
        return ResponseEntity.ok(ApiResponse.ok("Medicine added", dto));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineDTO>> update(
            @PathVariable Long id, @Valid @RequestBody MedicineRequest req) {
        return medicineRepository.findById(id).map(m -> {
            m.setName(req.getName()); m.setBrand(req.getBrand());
            m.setCategory(req.getCategory()); m.setDescription(req.getDescription());
            m.setPrice(req.getPrice()); m.setStockQuantity(req.getStockQuantity());
            m.setRequiresPrescription(req.isRequiresPrescription());
            MedicineDTO dto = MedicineDTO.from(medicineRepository.save(m));
            realtimeService.medicineUpdated(dto);
            return ResponseEntity.ok(ApiResponse.ok("Updated", dto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!medicineRepository.existsById(id)) return ResponseEntity.notFound().build();
        medicineRepository.deleteById(id);
        realtimeService.medicineDeleted(id);
        return ResponseEntity.ok(ApiResponse.ok("Medicine deleted", null));
    }

    @PatchMapping("/admin/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicineDTO>> updateStock(
            @PathVariable Long id, @RequestParam int quantity) {
        return medicineRepository.findById(id).map(m -> {
            m.setStockQuantity(quantity);
            MedicineDTO dto = MedicineDTO.from(medicineRepository.save(m));
            realtimeService.stockChanged(dto);
            return ResponseEntity.ok(ApiResponse.ok("Stock updated", dto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<MedicineDTO>>> lowStock() {
        List<MedicineDTO> list = medicineRepository.findByStockQuantityLessThan(15)
                .stream().map(MedicineDTO::from).toList();
        return ResponseEntity.ok(ApiResponse.ok("Low stock items", list));
    }
}
