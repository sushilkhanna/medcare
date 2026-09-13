package com.medcare.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;
    private BigDecimal price;

    public OrderItem() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Order order; private Medicine medicine;
        private Integer quantity; private BigDecimal price;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder order(Order order) { this.order = order; return this; }
        public Builder medicine(Medicine medicine) { this.medicine = medicine; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Builder price(BigDecimal price) { this.price = price; return this; }
        public OrderItem build() {
            OrderItem i = new OrderItem(); i.id = id; i.order = order;
            i.medicine = medicine; i.quantity = quantity; i.price = price; return i;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
