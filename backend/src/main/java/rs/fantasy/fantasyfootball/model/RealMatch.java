package rs.fantasy.fantasyfootball.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "real_matches")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RealMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "home_team_name", nullable = false)
    private String homeTeamName;  

    @Column(name = "away_team_name", nullable = false)
    private String awayTeamName;  

    @Column(name = "home_team_points")
    private Integer homeTeamPoints;

    @Column(name = "away_team_points")
    private Integer awayTeamPoints; 

    @Column(name = "winner_team")
    private String winnerTeam;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameweek_id", nullable = false)
    private Gameweek gameweek;

    public RealMatch() {}

    public RealMatch(String awayTeamName, String homeTeamName, Gameweek gameweek) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.gameweek = gameweek;
        this.homeTeamPoints = 0;
        this.awayTeamPoints = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Integer getHomeTeamPoints() {
        return homeTeamPoints;
    }

    public void setHomeTeamPoints(Integer homeTeamPoints) {
        this.homeTeamPoints = homeTeamPoints;
    }

    public Integer getAwayTeamPoints() {
        return awayTeamPoints;
    }

    public void setAwayTeamPoints(Integer awayTeamPoints) {
        this.awayTeamPoints = awayTeamPoints;
    }

    public String getWinnerTeam() {
        return winnerTeam;
    }

    public void setWinnerTeam(String winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public Gameweek getGameweek() {
        return gameweek;
    }

    public void setGameweek(Gameweek gameweek) {
        this.gameweek = gameweek;
    }
}

