package dev.aira.historico.entities;

import dev.aira.historico.dtos.UpdateConsultationInputDTO;
import dev.aira.historico.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "consultations")
public class Consultation {
    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    @Column(name = "consultation_date", nullable = false)
    private LocalDateTime consultationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus status;

    public boolean updateDate(LocalDateTime date) {
        if(date.isBefore(consultationDate)) {
            this.consultationDate = date;
            return true;
        }
        return false;
    }

    public void updateDoctor(User doctor) {
        this.doctorId = doctor.getId();
    }
}
