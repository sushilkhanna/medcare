package com.medcare.config;

import com.medcare.entity.*;
import com.medcare.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicineRepository medicineRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, DoctorRepository doctorRepository,
                      MedicineRepository medicineRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.medicineRepository = medicineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping.");
            return;
        }
        log.info("Seeding initial data...");

        // Admin
        userRepository.save(User.builder().name("Admin User").email("admin@medcare.com")
                .password(passwordEncoder.encode("admin123")).role(User.Role.ADMIN).enabled(true).build());

        // Doctors
        User du1 = userRepository.save(User.builder().name("Dr. Arjun Mehta").email("arjun@medcare.com")
                .password(passwordEncoder.encode("doctor123")).role(User.Role.DOCTOR).phone("9111111111").enabled(true).build());
        User du2 = userRepository.save(User.builder().name("Dr. Sneha Patel").email("sneha@medcare.com")
                .password(passwordEncoder.encode("doctor123")).role(User.Role.DOCTOR).phone("9222222222").enabled(true).build());
        User du3 = userRepository.save(User.builder().name("Dr. Rohit Sharma").email("rohit@medcare.com")
                .password(passwordEncoder.encode("doctor123")).role(User.Role.DOCTOR).phone("9333333333").enabled(true).build());
        User du4 = userRepository.save(User.builder().name("Dr. Kavita Singh").email("kavita@medcare.com")
                .password(passwordEncoder.encode("doctor123")).role(User.Role.DOCTOR).phone("9444444444").enabled(true).build());
        User du5 = userRepository.save(User.builder().name("Dr. Sameer Joshi").email("sameer@medcare.com")
                .password(passwordEncoder.encode("doctor123")).role(User.Role.DOCTOR).phone("9555555555").enabled(true).build());

        doctorRepository.save(Doctor.builder().user(du1).specialization("Cardiologist")
                .qualification("MBBS, MD Cardiology").experienceYears(12)
                .availability("Mon-Fri").rating(4.8).totalPatients(0).consultationFee("500").build());
        doctorRepository.save(Doctor.builder().user(du2).specialization("Dermatologist")
                .qualification("MBBS, MD Dermatology").experienceYears(8)
                .availability("Mon-Sat").rating(4.7).totalPatients(0).consultationFee("400").build());
        doctorRepository.save(Doctor.builder().user(du3).specialization("Orthopedic")
                .qualification("MBBS, MS Orthopedics").experienceYears(15)
                .availability("Tue-Sat").rating(4.9).totalPatients(0).consultationFee("600").build());
        doctorRepository.save(Doctor.builder().user(du4).specialization("Neurologist")
                .qualification("MBBS, DM Neurology").experienceYears(10)
                .availability("Mon-Thu").rating(4.6).totalPatients(0).consultationFee("700").build());
        doctorRepository.save(Doctor.builder().user(du5).specialization("General Physician")
                .qualification("MBBS, PGDM").experienceYears(6)
                .availability("All Days").rating(4.5).totalPatients(0).consultationFee("300").build());

        // Patients
        userRepository.save(User.builder().name("Priya Rao").email("patient@medcare.com")
                .password(passwordEncoder.encode("patient123")).role(User.Role.PATIENT)
                .phone("9876543210").address("42, MG Road, Dehradun").bloodGroup("B+").age(28).enabled(true).build());
        userRepository.save(User.builder().name("Ravi Kumar").email("ravi@medcare.com")
                .password(passwordEncoder.encode("patient123")).role(User.Role.PATIENT)
                .phone("9123456780").address("15, Rajpur Road, Dehradun").bloodGroup("O+").age(35).enabled(true).build());

        // Medicines
        Object[][] meds = {
            {"Paracetamol 500mg","Cipla","Analgesic","Relieves pain and fever",18.00,250,false},
            {"Azithromycin 250mg","Sun Pharma","Antibiotic","Treats bacterial infections",85.00,80,true},
            {"Vitamin D3 60K","Abbott","Supplement","Vitamin D deficiency treatment",120.00,150,false},
            {"Metformin 500mg","USV","Diabetes","Controls blood sugar levels",32.00,12,true},
            {"Omeprazole 20mg","Alkem","Antacid","Treats acid reflux and ulcers",45.00,95,false},
            {"Cetirizine 10mg","Mankind","Antihistamine","Relieves allergy symptoms",22.00,180,false},
            {"Amoxicillin 250mg","GSK","Antibiotic","Broad spectrum antibiotic",55.00,8,true},
            {"Atorvastatin 10mg","Pfizer","Cardiac","Reduces cholesterol levels",98.00,60,true},
            {"Pantoprazole 40mg","Zydus","Antacid","Proton pump inhibitor",38.00,120,false},
            {"Amlodipine 5mg","Cipla","Cardiac","Treats high blood pressure",52.00,75,true},
        };
        for (Object[] m : meds) {
            medicineRepository.save(Medicine.builder()
                    .name((String)m[0]).brand((String)m[1]).category((String)m[2])
                    .description((String)m[3])
                    .price(BigDecimal.valueOf((Double)m[4]))
                    .stockQuantity((Integer)m[5])
                    .requiresPrescription((Boolean)m[6])
                    .available(true).build());
        }
        log.info("Seeding complete! admin@medcare.com/admin123 | patient@medcare.com/patient123 | arjun@medcare.com/doctor123");
    }
}
