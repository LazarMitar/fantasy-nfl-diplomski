package rs.fantasy.fantasyfootball.dto;

public class SpecialTeamStatsRequest {
    private Double projectedPoints;
    private Double actualPoints;
    private Integer fieldGoalsMade;
    private Integer fieldGoalsMissed;
    private Integer extraPointsMade;
    private Integer extraPointsMissed;
    private Integer fieldGoalsOver50Yards;
    private Integer longestFieldGoal;

    public SpecialTeamStatsRequest() {}

    // Getters and Setters
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

    public Integer getFieldGoalsMade() {
        return fieldGoalsMade;
    }

    public void setFieldGoalsMade(Integer fieldGoalsMade) {
        this.fieldGoalsMade = fieldGoalsMade;
    }

    public Integer getFieldGoalsMissed() {
        return fieldGoalsMissed;
    }

    public void setFieldGoalsMissed(Integer fieldGoalsMissed) {
        this.fieldGoalsMissed = fieldGoalsMissed;
    }

    public Integer getExtraPointsMade() {
        return extraPointsMade;
    }

    public void setExtraPointsMade(Integer extraPointsMade) {
        this.extraPointsMade = extraPointsMade;
    }

    public Integer getExtraPointsMissed() {
        return extraPointsMissed;
    }

    public void setExtraPointsMissed(Integer extraPointsMissed) {
        this.extraPointsMissed = extraPointsMissed;
    }

    public Integer getFieldGoalsOver50Yards() {
        return fieldGoalsOver50Yards;
    }

    public void setFieldGoalsOver50Yards(Integer fieldGoalsOver50Yards) {
        this.fieldGoalsOver50Yards = fieldGoalsOver50Yards;
    }

    public Integer getLongestFieldGoal() {
        return longestFieldGoal;
    }

    public void setLongestFieldGoal(Integer longestFieldGoal) {
        this.longestFieldGoal = longestFieldGoal;
    }
}

