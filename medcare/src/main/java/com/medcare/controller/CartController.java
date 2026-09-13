package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.*;
import com.medcare.repository.*;
import com.medcare.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final JwtUtil jwtUtil;

    public CartController(CartRepository cartRepository, UserRepository userRepository,
                          MedicineRepository medicineRepository, JwtUtil jwtUtil) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
        this.jwtUtil = jwtUtil;
    }

    private User getUser(String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userRepository.findByEmail(email).orElseThrow();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartSummaryDTO>> getCart(
            @RequestHeader("Authorization") String auth) {
        User user = getUser(auth);
        List<Cart> items = cartRepository.findByUser(user);

        List<CartItemDTO> dtos = items.stream().map(c -> CartItemDTO.builder()
                .cartId(c.getId()).medicineId(c.getMedicine().getId())
                .medicineName(c.getMedicine().getName()).brand(c.getMedicine().getBrand())
                .category(c.getMedicine().getCategory()).price(c.getMedicine().getPrice())
                .quantity(c.getQuantity())
                .subtotal(c.getMedicine().getPrice().multiply(BigDecimal.valueOf(c.getQuantity())))
                .requiresPrescription(c.getMedicine().isRequiresPrescription())
                .build()).toList();

        BigDecimal subtotal = dtos.stream().map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal gst = subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(gst);
        int count = dtos.stream().mapToInt(CartItemDTO::getQuantity).sum();

        return ResponseEntity.ok(ApiResponse.ok("Cart fetched",
                CartSummaryDTO.builder().items(dtos).subtotal(subtotal)
                        .gst(gst).total(total).itemCount(count).build()));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addToCart(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody CartRequest req) {
        User user = getUser(auth);
        Medicine medicine = medicineRepository.findById(req.getMedicineId())
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (medicine.getStockQuantity() < req.getQuantity())
            return ResponseEntity.badRequest().body(ApiResponse.error("Insufficient stock"));

        cartRepository.findByUserAndMedicine(user, medicine).ifPresentOrElse(
                existing -> { existing.setQuantity(existing.getQuantity() + req.getQuantity());
                    cartRepository.save(existing); },
                () -> cartRepository.save(Cart.builder().user(user)
                        .medicine(medicine).quantity(req.getQuantity()).build()));

        return ResponseEntity.ok(ApiResponse.ok("Added to cart", null));
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse<String>> updateQuantity(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long cartId, @RequestParam int quantity) {
        User user = getUser(auth);
        return cartRepository.findById(cartId)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .map(c -> {
                    if (quantity <= 0) {
                        cartRepository.deleteById(cartId);
                        return ResponseEntity.ok(ApiResponse.<String>ok("Item removed", null));
                    }
                    c.setQuantity(quantity); cartRepository.save(c);
                    return ResponseEntity.ok(ApiResponse.<String>ok("Quantity updated", null));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<String>> removeItem(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long cartId) {
        User user = getUser(auth);
        return cartRepository.findById(cartId)
                .filter(c -> c.getUser().getId().equals(user.getId()))
                .map(c -> {
                    cartRepository.deleteById(cartId);
                    return ResponseEntity.ok(ApiResponse.<String>ok("Item removed", null));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(
            @RequestHeader("Authorization") String auth) {
        cartRepository.deleteByUser(getUser(auth));
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared", null));
    }
}
