package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_roster_player_id", nullable = false)
    private RosterPlayer initiatorRosterPlayer;

    @ManyToOne
    @JoinColumn(name = "receiver_roster_player_id", nullable = false)
    private RosterPlayer receiverRosterPlayer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime executedAt;

    public Trade() {}

    public Trade(RosterPlayer initiatorRosterPlayer, RosterPlayer receiverRosterPlayer) {
        this.initiatorRosterPlayer = initiatorRosterPlayer;
        this.receiverRosterPlayer = receiverRosterPlayer;
        this.status = TradeStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public RosterPlayer getInitiatorRosterPlayer() {
        return initiatorRosterPlayer;
    }

    public void setInitiatorRosterPlayer(RosterPlayer initiatorRosterPlayer) {
        this.initiatorRosterPlayer = initiatorRosterPlayer;
    }

    public RosterPlayer getReceiverRosterPlayer() {
        return receiverRosterPlayer;
    }

    public void setReceiverRosterPlayer(RosterPlayer receiverRosterPlayer) {
        this.receiverRosterPlayer = receiverRosterPlayer;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
