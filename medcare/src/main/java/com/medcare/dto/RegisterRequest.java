package com.medcare.dto;
import jakarta.validation.constraints.*;

public class RegisterRequest {
    @NotBlank public String name;
    @Email @NotBlank public String email;
    @NotBlank @Size(min=6) public String password;
    public String phone;
    public String role = "PATIENT";
    public RegisterRequest() {}
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
