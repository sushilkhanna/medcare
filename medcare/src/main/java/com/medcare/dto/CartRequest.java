package com.medcare.dto;
import jakarta.validation.constraints.*;

public class CartRequest {
    @NotNull public Long medicineId;
    @NotNull @Min(1) public Integer quantity;
    public CartRequest() {}
    public Long getMedicineId() { return medicineId; } public void setMedicineId(Long v) { this.medicineId = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
}
