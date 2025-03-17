package org.example.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "limits")
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Limit() {
    }

    public Limit(Long userId, BigDecimal value) {
        this.userId = userId;
        this.value = value;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
