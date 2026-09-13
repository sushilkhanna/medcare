package com.medcare.controller;

import com.medcare.dto.*;
import com.medcare.entity.User;
import com.medcare.repository.UserRepository;
import com.medcare.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            return ResponseEntity.badRequest().body(ApiResponse.error("Email already registered"));

        // SECURITY: only PATIENT/DOCTOR may self-register. Without this
        // check, a caller could POST {"role":"ADMIN"} and grant themselves
        // full admin access — role must never be trusted from the client
        // beyond this allow-list. Admin accounts are created only via the
        // DataSeeder or by an existing admin.
        User.Role role;
        try {
            role = User.Role.valueOf(req.getRole().toUpperCase());
            if (role == User.Role.ADMIN) role = User.Role.PATIENT;
        } catch (Exception e) {
            role = User.Role.PATIENT;
        }

        User user = User.builder().name(req.getName()).email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone()).role(role).enabled(true).build();
        userRepository.save(user);

        UserDetails ud = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(ud, user.getRole().name());
        return ResponseEntity.ok(ApiResponse.ok("Registration successful",
                AuthResponse.builder().token(token).role(user.getRole().name())
                        .userId(user.getId()).name(user.getName()).email(user.getEmail()).build()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (DisabledException e) {
            return ResponseEntity.status(403).body(ApiResponse.error("Your account has been suspended. Please contact support."));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid email or password"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Authentication failed: " + e.getMessage()));
        }
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        UserDetails ud = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(ud, user.getRole().name());
        return ResponseEntity.ok(ApiResponse.ok("Login successful",
                AuthResponse.builder().token(token).role(user.getRole().name())
                        .userId(user.getId()).name(user.getName()).email(user.getEmail()).build()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getMe(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(ApiResponse.ok("User fetched", UserDTO.from(user)));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody java.util.Map<String, Object> body) {
        String email = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email).orElseThrow();

        if (body.containsKey("name") && body.get("name") != null)
            user.setName((String) body.get("name"));
        if (body.containsKey("phone") && body.get("phone") != null)
            user.setPhone((String) body.get("phone"));
        if (body.containsKey("address") && body.get("address") != null)
            user.setAddress((String) body.get("address"));
        if (body.containsKey("bloodGroup") && body.get("bloodGroup") != null)
            user.setBloodGroup((String) body.get("bloodGroup"));
        if (body.containsKey("age") && body.get("age") != null)
            user.setAge(((Number) body.get("age")).intValue());

        userRepository.save(user);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", UserDTO.from(user)));
    }
}
