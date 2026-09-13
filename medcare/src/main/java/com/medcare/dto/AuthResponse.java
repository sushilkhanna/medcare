package com.medcare.dto;

public class AuthResponse {
    private String token; private String role;
    private Long userId; private String name; private String email;
    public AuthResponse() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token; private String role;
        private Long userId; private String name; private String email;
        public Builder token(String v) { this.token = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public Builder userId(Long v) { this.userId = v; return this; }
        public Builder name(String v) { this.name = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public AuthResponse build() {
            AuthResponse r = new AuthResponse(); r.token = token; r.role = role;
            r.userId = userId; r.name = name; r.email = email; return r;
        }
    }
    public String getToken() { return token; } public void setToken(String v) { this.token = v; }
    public String getRole() { return role; } public void setRole(String v) { this.role = v; }
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
}
