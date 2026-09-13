package com.medcare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PROCESSING;

    private String deliveryAddress;
    private LocalDateTime orderedAt;

    @PrePersist protected void onCreate() { orderedAt = LocalDateTime.now(); }

    public enum OrderStatus { PROCESSING, SHIPPED, DELIVERED, CANCELLED }

    public Order() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private List<OrderItem> orderItems;
        private BigDecimal totalAmount; private OrderStatus status = OrderStatus.PROCESSING;
        private String deliveryAddress;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder orderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(OrderStatus status) { this.status = status; return this; }
        public Builder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public Order build() {
            Order o = new Order(); o.id = id; o.user = user;
            o.orderItems = orderItems; o.totalAmount = totalAmount;
            o.status = status; o.deliveryAddress = deliveryAddress; return o;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
}
