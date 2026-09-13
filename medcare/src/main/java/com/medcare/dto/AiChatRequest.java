package com.medcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AiChatRequest {

    @NotBlank(message = "Message is required")
    @Size(max = 500, message = "Message must be under 500 characters")
    private String message;

    // Optional context so the assistant can be specific instead of generic.
    private String medicineName;
    private String medicineBrand;
    private String medicineCategory;

    public AiChatRequest() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getMedicineBrand() { return medicineBrand; }
    public void setMedicineBrand(String medicineBrand) { this.medicineBrand = medicineBrand; }
    public String getMedicineCategory() { return medicineCategory; }
    public void setMedicineCategory(String medicineCategory) { this.medicineCategory = medicineCategory; }
}
