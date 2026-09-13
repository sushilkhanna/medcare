package com.medcare.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {
    private Long id; private Long patientId; private String patientName;
    private Long doctorId; private String doctorName; private String specialization;
    private LocalDate appointmentDate; private LocalTime appointmentTime;
    private String reason; private String notes; private String status;
    private LocalDateTime bookedAt;
    public AppointmentDTO() {}
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id; private Long patientId; private String patientName;
        private Long doctorId; private String doctorName; private String specialization;
        private LocalDate appointmentDate; private LocalTime appointmentTime;
        private String reason; private String notes; private String status; private LocalDateTime bookedAt;
        public Builder id(Long v) { this.id = v; return this; }
        public Builder patientId(Long v) { this.patientId = v; return this; }
        public Builder patientName(String v) { this.patientName = v; return this; }
        public Builder doctorId(Long v) { this.doctorId = v; return this; }
        public Builder doctorName(String v) { this.doctorName = v; return this; }
        public Builder specialization(String v) { this.specialization = v; return this; }
        public Builder appointmentDate(LocalDate v) { this.appointmentDate = v; return this; }
        public Builder appointmentTime(LocalTime v) { this.appointmentTime = v; return this; }
        public Builder reason(String v) { this.reason = v; return this; }
        public Builder notes(String v) { this.notes = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder bookedAt(LocalDateTime v) { this.bookedAt = v; return this; }
        public AppointmentDTO build() {
            AppointmentDTO d = new AppointmentDTO(); d.id = id; d.patientId = patientId;
            d.patientName = patientName; d.doctorId = doctorId; d.doctorName = doctorName;
            d.specialization = specialization; d.appointmentDate = appointmentDate;
            d.appointmentTime = appointmentTime; d.reason = reason; d.notes = notes;
            d.status = status; d.bookedAt = bookedAt; return d;
        }
    }
    public Long getId() { return id; } public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; } public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; } public String getSpecialization() { return specialization; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public String getReason() { return reason; } public String getNotes() { return notes; }
    public String getStatus() { return status; } public LocalDateTime getBookedAt() { return bookedAt; }
    public void setId(Long v) { this.id = v; } public void setPatientId(Long v) { this.patientId = v; }
    public void setPatientName(String v) { this.patientName = v; } public void setDoctorId(Long v) { this.doctorId = v; }
    public void setDoctorName(String v) { this.doctorName = v; } public void setSpecialization(String v) { this.specialization = v; }
    public void setAppointmentDate(LocalDate v) { this.appointmentDate = v; }
    public void setAppointmentTime(LocalTime v) { this.appointmentTime = v; }
    public void setReason(String v) { this.reason = v; } public void setNotes(String v) { this.notes = v; }
    public void setStatus(String v) { this.status = v; } public void setBookedAt(LocalDateTime v) { this.bookedAt = v; }
}
