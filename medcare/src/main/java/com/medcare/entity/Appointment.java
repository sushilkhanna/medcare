package com.medcare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @ManyToOne @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false) private LocalDate appointmentDate;
    @Column(nullable = false) private LocalTime appointmentTime;
    private String reason;
    private String notes;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    private LocalDateTime bookedAt;

    @PrePersist protected void onCreate() { bookedAt = LocalDateTime.now(); }

    public enum AppointmentStatus { PENDING, CONFIRMED, REJECTED, COMPLETED, CANCELLED, EXPIRED }

    public Appointment() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private User patient; private Doctor doctor;
        private LocalDate appointmentDate; private LocalTime appointmentTime;
        private String reason; private String notes;
        private AppointmentStatus status = AppointmentStatus.PENDING;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder patient(User patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder appointmentDate(LocalDate v) { this.appointmentDate = v; return this; }
        public Builder appointmentTime(LocalTime v) { this.appointmentTime = v; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder status(AppointmentStatus status) { this.status = status; return this; }
        public Appointment build() {
            Appointment a = new Appointment(); a.id = id; a.patient = patient;
            a.doctor = doctor; a.appointmentDate = appointmentDate;
            a.appointmentTime = appointmentTime; a.reason = reason;
            a.notes = notes; a.status = status; return a;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getPatient() { return patient; }
    public void setPatient(User patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate v) { this.appointmentDate = v; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime v) { this.appointmentTime = v; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
}
