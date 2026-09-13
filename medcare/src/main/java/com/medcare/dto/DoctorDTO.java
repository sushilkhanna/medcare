package com.medcare.dto;
import com.medcare.entity.Doctor;

public class DoctorDTO {
    private Long id; private Long userId; private String name; private String email;
    private String phone; private String specialization; private String qualification;
    private String availability; private String consultationFee; private String bio;
    private Integer experienceYears; private Double rating; private Integer totalPatients;
    public DoctorDTO() {}
    public static DoctorDTO from(Doctor d) {
        DoctorDTO dto = new DoctorDTO();
        dto.id = d.getId(); dto.userId = d.getUser().getId();
        dto.name = d.getUser().getName(); dto.email = d.getUser().getEmail();
        dto.phone = d.getUser().getPhone(); dto.specialization = d.getSpecialization();
        dto.qualification = d.getQualification(); dto.availability = d.getAvailability();
        dto.consultationFee = d.getConsultationFee(); dto.experienceYears = d.getExperienceYears();
        dto.rating = d.getRating(); dto.totalPatients = d.getTotalPatients(); dto.bio = d.getBio();
        return dto;
    }
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id, userId; private String name, email, phone, specialization;
        private String qualification, availability, consultationFee, bio;
        private Integer experienceYears; private Double rating; private Integer totalPatients;
        public Builder id(Long v) { this.id = v; return this; } public Builder userId(Long v) { this.userId = v; return this; }
        public Builder name(String v) { this.name = v; return this; } public Builder email(String v) { this.email = v; return this; }
        public Builder phone(String v) { this.phone = v; return this; } public Builder specialization(String v) { this.specialization = v; return this; }
        public Builder qualification(String v) { this.qualification = v; return this; } public Builder availability(String v) { this.availability = v; return this; }
        public Builder consultationFee(String v) { this.consultationFee = v; return this; } public Builder bio(String v) { this.bio = v; return this; }
        public Builder experienceYears(Integer v) { this.experienceYears = v; return this; }
        public Builder rating(Double v) { this.rating = v; return this; } public Builder totalPatients(Integer v) { this.totalPatients = v; return this; }
        public DoctorDTO build() {
            DoctorDTO d = new DoctorDTO(); d.id = id; d.userId = userId; d.name = name; d.email = email;
            d.phone = phone; d.specialization = specialization; d.qualification = qualification;
            d.availability = availability; d.consultationFee = consultationFee; d.bio = bio;
            d.experienceYears = experienceYears; d.rating = rating; d.totalPatients = totalPatients; return d;
        }
    }
    public Long getId() { return id; } public Long getUserId() { return userId; }
    public String getName() { return name; } public String getEmail() { return email; }
    public String getPhone() { return phone; } public String getSpecialization() { return specialization; }
    public String getQualification() { return qualification; } public String getAvailability() { return availability; }
    public String getConsultationFee() { return consultationFee; } public String getBio() { return bio; }
    public Integer getExperienceYears() { return experienceYears; } public Double getRating() { return rating; }
    public Integer getTotalPatients() { return totalPatients; }
    public void setId(Long v){this.id=v;} public void setUserId(Long v){this.userId=v;}
    public void setName(String v){this.name=v;} public void setEmail(String v){this.email=v;}
    public void setPhone(String v){this.phone=v;} public void setSpecialization(String v){this.specialization=v;}
    public void setQualification(String v){this.qualification=v;} public void setAvailability(String v){this.availability=v;}
    public void setConsultationFee(String v){this.consultationFee=v;} public void setBio(String v){this.bio=v;}
    public void setExperienceYears(Integer v){this.experienceYears=v;} public void setRating(Double v){this.rating=v;}
    public void setTotalPatients(Integer v){this.totalPatients=v;}
}
