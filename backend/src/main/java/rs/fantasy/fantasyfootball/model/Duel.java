package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;

@Entity
@Table(
    name = "duels",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"home_roster_id", "away_roster_id", "gameweek_id"})
    }
)
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_roster_id", nullable = false)
    private Roster homeRoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_roster_id", nullable = false)
    private Roster awayRoster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameweek_id", nullable = false)
    private Gameweek gameweek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_roster_id")
    private Roster winnerRoster; // null dok se duel ne završi

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DuelStatus status = DuelStatus.PENDING;

    @Column(name = "home_points")
    private Double homePoints = 0.0;

    @Column(name = "away_points")
    private Double awayPoints = 0.0;

    public Duel() {}

    public Duel(Roster homeRoster, Roster awayRoster, Gameweek gameweek) {
        this.homeRoster = homeRoster;
        this.awayRoster = awayRoster;
        this.gameweek = gameweek;
        this.status = DuelStatus.PENDING;
    }

    // Validacija
    @PrePersist
    @PreUpdate
    private void validate() {
        if (homeRoster != null && homeRoster.equals(awayRoster)) {
            throw new IllegalStateException("Home and away rosters cannot be the same!");
        }
        if (homeRoster != null && awayRoster != null) {
            if (!homeRoster.getLeague().getId().equals(awayRoster.getLeague().getId())) {
                throw new IllegalStateException("Rosters must be from the same league!");
            }
        }
    }

    // Helper metode
    public boolean isDraw() {
        return status == DuelStatus.COMPLETED && homePoints.equals(awayPoints);
    }

    public Roster getLoser() {
        if (winnerRoster == null) return null;
        return winnerRoster.equals(homeRoster) ? awayRoster : homeRoster;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Roster getHomeRoster() {
        return homeRoster;
    }

    public void setHomeRoster(Roster homeRoster) {
        this.homeRoster = homeRoster;
    }

    public Roster getAwayRoster() {
        return awayRoster;
    }

    public void setAwayRoster(Roster awayRoster) {
        this.awayRoster = awayRoster;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public Roster getWinnerRoster() {
        return winnerRoster;
    }

    public void setWinnerRoster(Roster winnerRoster) {
        this.winnerRoster = winnerRoster;
    }

    public DuelStatus getStatus() {
        return status;
    }

    public void setStatus(DuelStatus status) {
        this.status = status;
    }

    public Double getHomePoints() {
        return homePoints;
    }

    public void setHomePoints(Double homePoints) {
        this.homePoints = homePoints;
    }

    public Double getAwayPoints() {
        return awayPoints;
    }

    public void setAwayPoints(Double awayPoints) {
        this.awayPoints = awayPoints;
    }

    @Override
    public String toString() {
        return "Duel{" +
                "id=" + id +
                ", home=" + (homeRoster != null ? homeRoster.getName() : "null") +
                ", away=" + (awayRoster != null ? awayRoster.getName() : "null") +
                ", gameweek=" + (gameweek != null ? gameweek.getWeekNumber() : "null") +
                ", status=" + status +
                ", score=" + homePoints + "-" + awayPoints +
                '}';
    }
}

