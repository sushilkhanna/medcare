package com.medcare.dto;
import com.medcare.entity.User;
import java.time.LocalDateTime;

public class UserDTO {
    private Long id; private String name; private String email;
    private String phone; private String address; private String bloodGroup;
    private Integer age; private String role; private boolean enabled;
    private LocalDateTime createdAt;

    public UserDTO() {}
    public static UserDTO from(User u) {
        UserDTO d = new UserDTO(); d.id = u.getId(); d.name = u.getName();
        d.email = u.getEmail(); d.phone = u.getPhone(); d.address = u.getAddress();
        d.bloodGroup = u.getBloodGroup(); d.age = u.getAge();
        d.role = u.getRole().name(); d.enabled = u.isEnabled();
        d.createdAt = u.getCreatedAt(); return d;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String v) { this.name = v; }
    public String getEmail() { return email; } public void setEmail(String v) { this.email = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { this.phone = v; }
    public String getAddress() { return address; } public void setAddress(String v) { this.address = v; }
    public String getBloodGroup() { return bloodGroup; } public void setBloodGroup(String v) { this.bloodGroup = v; }
    public Integer getAge() { return age; } public void setAge(Integer v) { this.age = v; }
    public String getRole() { return role; } public void setRole(String v) { this.role = v; }
    public boolean isEnabled() { return enabled; } public void setEnabled(boolean v) { this.enabled = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
