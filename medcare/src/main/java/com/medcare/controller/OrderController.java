package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.*;
import com.medcare.repository.*;
import com.medcare.security.JwtUtil;
import com.medcare.service.RealtimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;
    private final JwtUtil jwtUtil;
    private final RealtimeService realtimeService;

    public OrderController(OrderRepository orderRepository, CartRepository cartRepository,
                           UserRepository userRepository, MedicineRepository medicineRepository,
                           JwtUtil jwtUtil, RealtimeService realtimeService) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
        this.jwtUtil = jwtUtil;
        this.realtimeService = realtimeService;
    }

    private User getUser(String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userRepository.findByEmail(email).orElseThrow();
    }

    private OrderDTO toDTO(Order o) {
        List<OrderItemDTO> items = (o.getOrderItems() == null) ? List.of() :
                o.getOrderItems().stream().map(i -> OrderItemDTO.builder()
                        .medicineId(i.getMedicine().getId())
                        .medicineName(i.getMedicine().getName())
                        .brand(i.getMedicine().getBrand())
                        .quantity(i.getQuantity()).price(i.getPrice())
                        .subtotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                        .build()).toList();
        return OrderDTO.builder().id(o.getId()).status(o.getStatus().name())
                .totalAmount(o.getTotalAmount()).deliveryAddress(o.getDeliveryAddress())
                .orderedAt(o.getOrderedAt()).orderItems(items)
                .itemCount(items.stream().mapToInt(OrderItemDTO::getQuantity).sum()).build();
    }

    @PostMapping("/checkout")
    @Transactional
    public ResponseEntity<ApiResponse<OrderDTO>> checkout(
            @RequestHeader("Authorization") String auth) {
        User user = getUser(auth);
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty())
            return ResponseEntity.badRequest().body(ApiResponse.error("Cart is empty"));

        Order order = Order.builder().user(user)
                .deliveryAddress(user.getAddress() != null ? user.getAddress() : "Not specified")
                .status(Order.OrderStatus.PROCESSING).build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Cart cart : cartItems) {
            Medicine med = cart.getMedicine();
            if (med.getStockQuantity() < cart.getQuantity())
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Insufficient stock for: " + med.getName()));
            med.setStockQuantity(med.getStockQuantity() - cart.getQuantity());
            medicineRepository.save(med);
            realtimeService.stockChanged(MedicineDTO.from(med));
            OrderItem item = OrderItem.builder().order(order).medicine(med)
                    .quantity(cart.getQuantity()).price(med.getPrice()).build();
            orderItems.add(item);
            subtotal = subtotal.add(med.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        BigDecimal gst = subtotal.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(subtotal.add(gst));
        order.setOrderItems(orderItems);
        Order saved = orderRepository.save(order);
        cartRepository.deleteByUser(user);
        return ResponseEntity.ok(ApiResponse.ok("Order placed successfully", toDTO(saved)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> myOrders(
            @RequestHeader("Authorization") String auth) {
        List<OrderDTO> orders = orderRepository.findByUserOrderByOrderedAtDesc(getUser(auth))
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(
            @RequestHeader("Authorization") String auth, @PathVariable Long id) {
        User user = getUser(auth);
        return orderRepository.findById(id)
                .filter(o -> o.getUser().getId().equals(user.getId()))
                .map(o -> ResponseEntity.ok(ApiResponse.ok("Order found", toDTO(o))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> allOrders() {
        List<OrderDTO> orders = orderRepository.findAll().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(ApiResponse.ok("All orders", orders));
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDTO>> updateStatus(
            @PathVariable Long id, @RequestParam String status) {
        return orderRepository.findById(id).map(o -> {
            o.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
            Order saved = orderRepository.save(o);
            realtimeService.orderStatusChanged(saved.getUser().getId(), saved.getId(), saved.getStatus().name());
            return ResponseEntity.ok(ApiResponse.ok("Status updated", toDTO(saved)));
        }).orElse(ResponseEntity.notFound().build());
    }
}
