package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "special_team_stats")
@DiscriminatorValue("SPECIAL_TEAM")
public class SpecialTeamStats extends PlayerGameweekStats {

    private int fieldGoalsMade;
    private int fieldGoalsMissed;
    private int extraPointsMade;
    private int extraPointsMissed;
    private int fieldGoalsOver50Yards;
    private int longestFieldGoal;

    public SpecialTeamStats() {}

    public SpecialTeamStats(Player player, Gameweek gameweek) {
        super(null, player, gameweek, 0.0, 0.0);
    }

    @Override
    public void calculatePoints() {
        double points = 0;
        points += fieldGoalsMade * 3;
        points += fieldGoalsOver50Yards * 2;
        points -= fieldGoalsMissed * 1;
        points += extraPointsMade * 1;
        points -= extraPointsMissed * 1;

        setActualPoints(points);
    }

    // Getters and Setters
    public int getFieldGoalsMade() {
        return fieldGoalsMade;
    }

    public void setFieldGoalsMade(int fieldGoalsMade) {
        this.fieldGoalsMade = fieldGoalsMade;
    }

    public int getFieldGoalsMissed() {
        return fieldGoalsMissed;
    }

    public void setFieldGoalsMissed(int fieldGoalsMissed) {
        this.fieldGoalsMissed = fieldGoalsMissed;
    }

    public int getExtraPointsMade() {
        return extraPointsMade;
    }

    public void setExtraPointsMade(int extraPointsMade) {
        this.extraPointsMade = extraPointsMade;
    }

    public int getExtraPointsMissed() {
        return extraPointsMissed;
    }

    public void setExtraPointsMissed(int extraPointsMissed) {
        this.extraPointsMissed = extraPointsMissed;
    }

    public int getFieldGoalsOver50Yards() {
        return fieldGoalsOver50Yards;
    }

    public void setFieldGoalsOver50Yards(int fieldGoalsOver50Yards) {
        this.fieldGoalsOver50Yards = fieldGoalsOver50Yards;
    }

    public int getLongestFieldGoal() {
        return longestFieldGoal;
    }

    public void setLongestFieldGoal(int longestFieldGoal) {
        this.longestFieldGoal = longestFieldGoal;
    }
}
