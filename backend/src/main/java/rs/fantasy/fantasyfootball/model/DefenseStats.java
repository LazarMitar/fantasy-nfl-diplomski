package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "defense_stats")
@DiscriminatorValue("DEFENSE")
public class DefenseStats extends PlayerGameweekStats {

    private int sacks;
    private int interceptions;
    private int fumblesRecovered;
    private int touchdowns;
    private int pointsAllowed;
    private int saftey;

    public DefenseStats() {}

    public DefenseStats(Player player, Gameweek gameweek) {
        super(null, player, gameweek, 0.0, 0.0);
    }

    @Override
    public void calculatePoints() {
        double points = 0;
        points += sacks * 1;
        points += interceptions * 2;
        points += fumblesRecovered * 2;
        points += touchdowns * 6;
        points += saftey * 2;

        // negative points for points allowed
        if (pointsAllowed == 0) points += 10;
        else if (pointsAllowed <= 6) points += 7;
        else if (pointsAllowed <= 13) points += 4;
        else if (pointsAllowed <= 20) points += 1;
        else if (pointsAllowed <= 27) points += 0;
        else if (pointsAllowed <= 34) points -= 1;
        else points -= 4;

        setActualPoints(points);
    }

    // Getters and Setters
    public int getSacks() {
        return sacks;
    }

    public void setSacks(int sacks) {
        this.sacks = sacks;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public int getFumblesRecovered() {
        return fumblesRecovered;
    }

    public void setFumblesRecovered(int fumblesRecovered) {
        this.fumblesRecovered = fumblesRecovered;
    }

    public int getTouchdowns() {
        return touchdowns;
    }

    public void setTouchdowns(int touchdowns) {
        this.touchdowns = touchdowns;
    }

    public int getPointsAllowed() {
        return pointsAllowed;
    }

    public void setPointsAllowed(int pointsAllowed) {
        this.pointsAllowed = pointsAllowed;
    }

    public int getSaftey() {
        return saftey;
    }

    public void setSaftey(int saftey) {
        this.saftey = saftey;
    }
}
