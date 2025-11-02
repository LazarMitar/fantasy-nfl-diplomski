package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "player_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "player_gameweek_stats")
public abstract class PlayerGameweekStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameweek_id", nullable = false)
    private Gameweek gameweek;

    @Column(name = "projected_points")
    private Double projectedPoints;

    @Column(name = "actual_points")
    private Double actualPoints;

    public PlayerGameweekStats() {}

    public PlayerGameweekStats(Long id, Player player, Gameweek gameweek, Double projectedPoints, Double actualPoints) {
        this.id = id;
        this.player = player;
        this.gameweek = gameweek;
        this.projectedPoints = projectedPoints;
        this.actualPoints = actualPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }

    public Double getProjectedPoints() {
        return projectedPoints;
    }

    public void setProjectedPoints(Double projectedPoints) {
        this.projectedPoints = projectedPoints;
    }

    public Double getActualPoints() {
        return actualPoints;
    }

    public void setActualPoints(Double actualPoints) {
        this.actualPoints = actualPoints;
    }

    public abstract void calculatePoints();
}
