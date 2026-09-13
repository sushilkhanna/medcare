package com.medcare.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class MedicineRequest {
    @NotBlank public String name;
    @NotBlank public String brand;
    public String category; public String description;
    @NotNull @Positive public BigDecimal price;
    @NotNull @Min(0) public Integer stockQuantity;
    public boolean requiresPrescription;
    public MedicineRequest() {}
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getBrand() { return brand; } public void setBrand(String v) { this.brand = v; }
    public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal v) { this.price = v; }
    public Integer getStockQuantity() { return stockQuantity; } public void setStockQuantity(Integer v) { this.stockQuantity = v; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean v) { this.requiresPrescription = v; }
}
