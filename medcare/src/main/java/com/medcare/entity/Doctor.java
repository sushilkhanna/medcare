package com.medcare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private String availability;
    private Double rating = 0.0;
    private Integer totalPatients = 0;
    private String consultationFee;
    private String bio;

    public Doctor() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User user; private String specialization;
        private String qualification; private Integer experienceYears;
        private String availability; private Double rating = 0.0;
        private Integer totalPatients = 0; private String consultationFee; private String bio;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder specialization(String v) { this.specialization = v; return this; }
        public Builder qualification(String v) { this.qualification = v; return this; }
        public Builder experienceYears(Integer v) { this.experienceYears = v; return this; }
        public Builder availability(String v) { this.availability = v; return this; }
        public Builder rating(Double v) { this.rating = v; return this; }
        public Builder totalPatients(Integer v) { this.totalPatients = v; return this; }
        public Builder consultationFee(String v) { this.consultationFee = v; return this; }
        public Builder bio(String v) { this.bio = v; return this; }
        public Doctor build() {
            Doctor d = new Doctor(); d.id = id; d.user = user;
            d.specialization = specialization; d.qualification = qualification;
            d.experienceYears = experienceYears; d.availability = availability;
            d.rating = rating; d.totalPatients = totalPatients;
            d.consultationFee = consultationFee; d.bio = bio; return d;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String v) { this.specialization = v; }
    public String getQualification() { return qualification; }
    public void setQualification(String v) { this.qualification = v; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer v) { this.experienceYears = v; }
    public String getAvailability() { return availability; }
    public void setAvailability(String v) { this.availability = v; }
    public Double getRating() { return rating; }
    public void setRating(Double v) { this.rating = v; }
    public Integer getTotalPatients() { return totalPatients; }
    public void setTotalPatients(Integer v) { this.totalPatients = v; }
    public String getConsultationFee() { return consultationFee; }
    public void setConsultationFee(String v) { this.consultationFee = v; }
    public String getBio() { return bio; }
    public void setBio(String v) { this.bio = v; }
}
