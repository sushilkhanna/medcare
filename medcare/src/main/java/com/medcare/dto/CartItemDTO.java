package com.medcare.dto;
import java.math.BigDecimal;

public class CartItemDTO {
    private Long cartId; private Long medicineId; private String medicineName;
    private String brand; private String category; private BigDecimal price;
    private Integer quantity; private BigDecimal subtotal; private boolean requiresPrescription;

    public CartItemDTO() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long cartId; private Long medicineId; private String medicineName;
        private String brand; private String category; private BigDecimal price;
        private Integer quantity; private BigDecimal subtotal; private boolean requiresPrescription;
        public Builder cartId(Long v) { this.cartId = v; return this; }
        public Builder medicineId(Long v) { this.medicineId = v; return this; }
        public Builder medicineName(String v) { this.medicineName = v; return this; }
        public Builder brand(String v) { this.brand = v; return this; }
        public Builder category(String v) { this.category = v; return this; }
        public Builder price(BigDecimal v) { this.price = v; return this; }
        public Builder quantity(Integer v) { this.quantity = v; return this; }
        public Builder subtotal(BigDecimal v) { this.subtotal = v; return this; }
        public Builder requiresPrescription(boolean v) { this.requiresPrescription = v; return this; }
        public CartItemDTO build() {
            CartItemDTO d = new CartItemDTO(); d.cartId = cartId; d.medicineId = medicineId;
            d.medicineName = medicineName; d.brand = brand; d.category = category;
            d.price = price; d.quantity = quantity; d.subtotal = subtotal;
            d.requiresPrescription = requiresPrescription; return d;
        }
    }
    public Long getCartId() { return cartId; } public Long getMedicineId() { return medicineId; }
    public String getMedicineName() { return medicineName; } public String getBrand() { return brand; }
    public String getCategory() { return category; } public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; } public BigDecimal getSubtotal() { return subtotal; }
    public boolean isRequiresPrescription() { return requiresPrescription; }
    public void setCartId(Long v) { this.cartId = v; } public void setMedicineId(Long v) { this.medicineId = v; }
    public void setMedicineName(String v) { this.medicineName = v; } public void setBrand(String v) { this.brand = v; }
    public void setCategory(String v) { this.category = v; } public void setPrice(BigDecimal v) { this.price = v; }
    public void setQuantity(Integer v) { this.quantity = v; } public void setSubtotal(BigDecimal v) { this.subtotal = v; }
    public void setRequiresPrescription(boolean v) { this.requiresPrescription = v; }
}
