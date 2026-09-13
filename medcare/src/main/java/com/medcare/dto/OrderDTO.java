package com.medcare.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id; private String status; private BigDecimal totalAmount;
    private String deliveryAddress; private LocalDateTime orderedAt;
    private List<OrderItemDTO> orderItems; private int itemCount;
    public OrderDTO() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private String status; private BigDecimal totalAmount;
        private String deliveryAddress; private LocalDateTime orderedAt;
        private List<OrderItemDTO> orderItems; private int itemCount;
        public Builder id(Long v) { this.id = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder totalAmount(BigDecimal v) { this.totalAmount = v; return this; }
        public Builder deliveryAddress(String v) { this.deliveryAddress = v; return this; }
        public Builder orderedAt(LocalDateTime v) { this.orderedAt = v; return this; }
        public Builder orderItems(List<OrderItemDTO> v) { this.orderItems = v; return this; }
        public Builder itemCount(int v) { this.itemCount = v; return this; }
        public OrderDTO build() {
            OrderDTO d = new OrderDTO(); d.id = id; d.status = status;
            d.totalAmount = totalAmount; d.deliveryAddress = deliveryAddress;
            d.orderedAt = orderedAt; d.orderItems = orderItems; d.itemCount = itemCount; return d;
        }
    }
    public Long getId() { return id; } public void setId(Long v) { this.id = v; }
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal v) { this.totalAmount = v; }
    public String getDeliveryAddress() { return deliveryAddress; } public void setDeliveryAddress(String v) { this.deliveryAddress = v; }
    public LocalDateTime getOrderedAt() { return orderedAt; } public void setOrderedAt(LocalDateTime v) { this.orderedAt = v; }
    public List<OrderItemDTO> getOrderItems() { return orderItems; } public void setOrderItems(List<OrderItemDTO> v) { this.orderItems = v; }
    public int getItemCount() { return itemCount; } public void setItemCount(int v) { this.itemCount = v; }
}
