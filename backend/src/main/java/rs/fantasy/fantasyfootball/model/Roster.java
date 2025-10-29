package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rosters")
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int wins;
    @Column(nullable = false)
    private int losses;
    @Column(nullable = false)
    private double points;
    @Column(nullable = false)
    private double budget;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_kor", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "league_id", referencedColumnName = "id", nullable = false)
    private League league;

    public Roster() {}

    public Roster(String name, User user, League league) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.points = 0.0;
        this.budget = 150.0;
        this.user = user;
        this.league = league;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
