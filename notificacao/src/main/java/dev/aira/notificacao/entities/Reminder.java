package dev.aira.notificacao.entities;

import dev.aira.notificacao.enums.ReminderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID consultationId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderType type;

    @Column(nullable = false)
    private LocalDateTime scheduledTo;

    @Column(nullable = false)
    private boolean sent;

    public Reminder(UUID consultationId, UUID userId, ReminderType type, LocalDateTime scheduledTo, boolean sent) {
        this.consultationId = consultationId;
        this.userId = userId;
        this.type = type;
        this.scheduledTo = scheduledTo;
        this.sent = sent;
    }

    public void markAsSent() {
        sent = true;
    }
}