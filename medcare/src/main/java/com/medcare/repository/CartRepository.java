package com.medcare.repository;

import com.medcare.entity.Cart;
import com.medcare.entity.Medicine;
import com.medcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Optional<Cart> findByUserAndMedicine(User user, Medicine medicine);
    @Transactional
    void deleteByUser(User user);
}
