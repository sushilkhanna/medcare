package com.medcare.repository;

import com.medcare.entity.Order;
import com.medcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderedAtDesc(User user);
    List<Order> findByStatus(Order.OrderStatus status);
}
