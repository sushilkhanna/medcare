package com.medcare.dto;
import java.math.BigDecimal;

public class OrderItemDTO {
    private Long medicineId; private String medicineName; private String brand;
    private Integer quantity; private BigDecimal price; private BigDecimal subtotal;
    public OrderItemDTO() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long medicineId; private String medicineName; private String brand;
        private Integer quantity; private BigDecimal price; private BigDecimal subtotal;
        public Builder medicineId(Long v) { this.medicineId = v; return this; }
        public Builder medicineName(String v) { this.medicineName = v; return this; }
        public Builder brand(String v) { this.brand = v; return this; }
        public Builder quantity(Integer v) { this.quantity = v; return this; }
        public Builder price(BigDecimal v) { this.price = v; return this; }
        public Builder subtotal(BigDecimal v) { this.subtotal = v; return this; }
        public OrderItemDTO build() {
            OrderItemDTO d = new OrderItemDTO(); d.medicineId = medicineId;
            d.medicineName = medicineName; d.brand = brand; d.quantity = quantity;
            d.price = price; d.subtotal = subtotal; return d;
        }
    }
    public Long getMedicineId() { return medicineId; } public void setMedicineId(Long v) { this.medicineId = v; }
    public String getMedicineName() { return medicineName; } public void setMedicineName(String v) { this.medicineName = v; }
    public String getBrand() { return brand; } public void setBrand(String v) { this.brand = v; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer v) { this.quantity = v; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal v) { this.price = v; }
    public BigDecimal getSubtotal() { return subtotal; } public void setSubtotal(BigDecimal v) { this.subtotal = v; }
}
