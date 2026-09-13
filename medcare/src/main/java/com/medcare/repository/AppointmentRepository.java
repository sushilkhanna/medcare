package com.medcare.repository;

import com.medcare.entity.Appointment;
import com.medcare.entity.Doctor;
import com.medcare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientOrderByAppointmentDateDesc(User patient);
    List<Appointment> findByDoctorOrderByAppointmentDateAsc(Doctor doctor);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    List<Appointment> findByDoctorAndStatus(Doctor doctor, Appointment.AppointmentStatus status);

    // Used by the midnight auto-expiry job: any appointment still awaiting a
    // doctor's decision whose date has already passed.
    List<Appointment> findByStatusAndAppointmentDateBefore(
            Appointment.AppointmentStatus status, LocalDate date);

    List<Appointment> findByDoctorAndAppointmentDateAndAppointmentTime(
            Doctor doctor, LocalDate appointmentDate, java.time.LocalTime appointmentTime);
}
