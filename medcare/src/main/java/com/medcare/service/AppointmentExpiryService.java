package com.medcare.service;

import com.medcare.entity.Appointment;
import com.medcare.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Runs at midnight every night and auto-expires any appointment request that
 * a doctor never responded to (still PENDING) whose requested date has
 * already passed. This stops stale requests from sitting in a doctor's queue
 * forever and lets the patient see clearly that the slot is gone rather than
 * leaving it ambiguously "pending".
 *
 * Only PENDING appointments are touched — CONFIRMED, REJECTED, CANCELLED and
 * COMPLETED are left alone.
 */
@Service
public class AppointmentExpiryService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentExpiryService.class);

    private final AppointmentRepository appointmentRepository;

    public AppointmentExpiryService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Cron: second minute hour day month day-of-week — fires at 00:00:00
    // every day, in the server's local timezone.
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireStalePendingAppointments() {
        List<Appointment> stale = appointmentRepository
                .findByStatusAndAppointmentDateBefore(Appointment.AppointmentStatus.PENDING, LocalDate.now());

        if (stale.isEmpty()) return;

        for (Appointment a : stale) {
            a.setStatus(Appointment.AppointmentStatus.EXPIRED);
            a.setNotes("Automatically expired — the requested date passed with no response from the doctor.");
        }
        appointmentRepository.saveAll(stale);
        log.info("Auto-expired {} stale pending appointment(s) at midnight.", stale.size());
    }
}
