package rs.fantasy.fantasyfootball.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "match_predictions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "real_match_id"})
    }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user"})
public class MatchPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id_kor", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "real_match_id", nullable = false)
    private RealMatch realMatch;

    @Column(name = "predicted_winner", nullable = false)
    private String predictedWinner;  

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PredictionStatus status = PredictionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MatchPrediction() {}

    public MatchPrediction(User user, RealMatch realMatch, String predictedWinner) {
        this.user = user;
        this.realMatch = realMatch;
        this.predictedWinner = predictedWinner;
        this.status = PredictionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealMatch getRealMatch() {
        return realMatch;
    }

    public void setRealMatch(RealMatch realMatch) {
        this.realMatch = realMatch;
    }

    public String getPredictedWinner() {
        return predictedWinner;
    }

    public void setPredictedWinner(String predictedWinner) {
        this.predictedWinner = predictedWinner;
    }

    public PredictionStatus getStatus() {
        return status;
    }

    public void setStatus(PredictionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

