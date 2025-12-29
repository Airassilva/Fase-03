package dev.aira.notificacao.repositories;

import dev.aira.notificacao.entities.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, UUID> {
    @Query("""
        select r
        from Reminder r
        where r.sent = false
          and r.scheduledTo <= :now
    """)
    List<Reminder> findPendingForNow(@Param("now") LocalDateTime now);
}
