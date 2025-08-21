package dev.gababartnicka.kotlinwebjourney.tasks;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    private boolean done;

    public void markAsDone() {
        this.done = true;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", done=" + done +
                '}';
    }
}
