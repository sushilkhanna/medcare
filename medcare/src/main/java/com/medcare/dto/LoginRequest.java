package com.medcare.dto;
import jakarta.validation.constraints.*;

public class LoginRequest {
    @Email @NotBlank public String email;
    @NotBlank public String password;
    public LoginRequest() {}
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
