package rs.fantasy.fantasyfootball.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import rs.fantasy.fantasyfootball.model.GameweekStatus;

@Entity
@Table(name = "gameweeks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Gameweek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int weekNumber;

    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameweekStatus status;

    public Gameweek() {}

    public Gameweek(int weekNumber, String season, LocalDateTime startTime, LocalDateTime endTime) {
        this.weekNumber = weekNumber;
        this.season = season;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = GameweekStatus.NOT_STARTED_YET;
    }

    // Getteri i setteri
    public Long getId() { return id; }

    public int getWeekNumber() { return weekNumber; }
    public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public GameweekStatus getStatus() { return status; }
    public void setStatus(GameweekStatus status) { this.status = status; }
}
