package com.medcare.dto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest {
    @NotNull public Long doctorId;
    @NotNull public LocalDate appointmentDate;
    @NotNull public LocalTime appointmentTime;
    public String reason;
    public AppointmentRequest() {}
    public Long getDoctorId() { return doctorId; } public void setDoctorId(Long v) { this.doctorId = v; }
    public LocalDate getAppointmentDate() { return appointmentDate; } public void setAppointmentDate(LocalDate v) { this.appointmentDate = v; }
    public LocalTime getAppointmentTime() { return appointmentTime; } public void setAppointmentTime(LocalTime v) { this.appointmentTime = v; }
    public String getReason() { return reason; } public void setReason(String v) { this.reason = v; }
}
