package com.medcare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(nullable = false)
    private Integer quantity;

    public Cart() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private Medicine medicine; private Integer quantity;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder medicine(Medicine medicine) { this.medicine = medicine; return this; }
        public Builder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public Cart build() {
            Cart c = new Cart(); c.id = id; c.user = user;
            c.medicine = medicine; c.quantity = quantity; return c;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Medicine getMedicine() { return medicine; }
    public void setMedicine(Medicine medicine) { this.medicine = medicine; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
