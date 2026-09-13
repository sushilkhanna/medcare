package com.medcare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines")
public class Medicine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String brand;
    private String category;
    private String description;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(nullable = false) private Integer stockQuantity;
    private String imageUrl;
    private boolean requiresPrescription = false;
    @Column(nullable = false) private boolean available = true;
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Medicine() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String name; private String brand;
        private String category; private String description; private BigDecimal price;
        private Integer stockQuantity; private String imageUrl;
        private boolean requiresPrescription = false; private boolean available = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String v) { this.name = v; return this; }
        public Builder brand(String v) { this.brand = v; return this; }
        public Builder category(String v) { this.category = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder price(BigDecimal v) { this.price = v; return this; }
        public Builder stockQuantity(Integer v) { this.stockQuantity = v; return this; }
        public Builder imageUrl(String v) { this.imageUrl = v; return this; }
        public Builder requiresPrescription(boolean v) { this.requiresPrescription = v; return this; }
        public Builder available(boolean v) { this.available = v; return this; }
        public Medicine build() {
            Medicine m = new Medicine(); m.id = id; m.name = name; m.brand = brand;
            m.category = category; m.description = description; m.price = price;
            m.stockQuantity = stockQuantity; m.imageUrl = imageUrl;
            m.requiresPrescription = requiresPrescription; m.available = available; return m;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setRequiresPrescription(boolean v) { this.requiresPrescription = v; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
