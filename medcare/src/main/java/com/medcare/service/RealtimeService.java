package com.medcare.service;

import com.medcare.dto.MedicineDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Thin wrapper around SimpMessagingTemplate so controllers don't need to
 * know STOMP destination names directly. Every event is a small JSON
 * envelope: { "type": "...", "medicine": {...} } so the frontend can
 * dispatch on `type` and patch the DOM instead of re-fetching everything.
 */
@Service
public class RealtimeService {

    private final SimpMessagingTemplate messagingTemplate;

    public RealtimeService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void medicineAdded(MedicineDTO medicine) {
        send("MEDICINE_ADDED", medicine);
    }

    public void medicineUpdated(MedicineDTO medicine) {
        send("MEDICINE_UPDATED", medicine);
    }

    public void medicineDeleted(Long medicineId) {
        messagingTemplate.convertAndSend("/topic/medicines",
                new Event("MEDICINE_DELETED", medicineId, null));
    }

    public void stockChanged(MedicineDTO medicine) {
        send("STOCK_CHANGED", medicine);
    }

    /** Notify a specific patient (by user id) that one of their orders changed status. */
    public void orderStatusChanged(Long userId, Long orderId, String status) {
        messagingTemplate.convertAndSend("/topic/orders/" + userId,
                new OrderEvent(orderId, status));
    }

    public static class OrderEvent {
        public Long orderId;
        public String status;
        public OrderEvent() {}
        public OrderEvent(Long orderId, String status) { this.orderId = orderId; this.status = status; }
    }

    private void send(String type, MedicineDTO medicine) {
        messagingTemplate.convertAndSend("/topic/medicines",
                new Event(type, medicine.getId(), medicine));
    }

    /** Simple broadcast envelope. */
    public static class Event {
        public String type;
        public Long medicineId;
        public MedicineDTO medicine;

        public Event() {}
        public Event(String type, Long medicineId, MedicineDTO medicine) {
            this.type = type; this.medicineId = medicineId; this.medicine = medicine;
        }
    }
}
