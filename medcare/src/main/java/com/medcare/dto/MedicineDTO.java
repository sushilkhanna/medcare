package com.medcare.dto;
import com.medcare.entity.Medicine;
import java.math.BigDecimal;

public class MedicineDTO {
    private Long id; private String name; private String brand;
    private String category; private String description; private String imageUrl;
    private BigDecimal price; private Integer stockQuantity;
    private boolean requiresPrescription; private boolean available;

    public MedicineDTO() {}
    public static MedicineDTO from(Medicine m) {
        MedicineDTO d = new MedicineDTO(); d.id = m.getId(); d.name = m.getName();
        d.brand = m.getBrand(); d.category = m.getCategory();
        d.description = m.getDescription(); d.imageUrl = m.getImageUrl();
        d.price = m.getPrice(); d.stockQuantity = m.getStockQuantity();
        d.requiresPrescription = m.isRequiresPrescription(); d.available = m.isAvailable(); return d;
    }
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getBrand() { return brand; } public void setBrand(String v) { this.brand = v; }
    public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String v) { this.imageUrl = v; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal v) { this.price = v; }
    public Integer getStockQuantity() { return stockQuantity; } public void setStockQuantity(Integer v) { this.stockQuantity = v; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean v) { this.requiresPrescription = v; }
    public boolean isAvailable() { return available; } public void setAvailable(boolean v) { this.available = v; }
}
