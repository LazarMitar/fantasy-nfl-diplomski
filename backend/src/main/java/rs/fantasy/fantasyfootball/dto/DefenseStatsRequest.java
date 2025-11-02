package rs.fantasy.fantasyfootball.dto;

public class DefenseStatsRequest {
    private Double projectedPoints;
    private Double actualPoints;
    private Integer sacks;
    private Integer interceptions;
    private Integer fumblesRecovered;
    private Integer touchdowns;
    private Integer pointsAllowed;
    private Integer saftey;

    public DefenseStatsRequest() {}

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

    public Integer getSacks() {
        return sacks;
    }

    public void setSacks(Integer sacks) {
        this.sacks = sacks;
    }

    public Integer getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(Integer interceptions) {
        this.interceptions = interceptions;
    }

    public Integer getFumblesRecovered() {
        return fumblesRecovered;
    }

    public void setFumblesRecovered(Integer fumblesRecovered) {
        this.fumblesRecovered = fumblesRecovered;
    }

    public Integer getTouchdowns() {
        return touchdowns;
    }

    public void setTouchdowns(Integer touchdowns) {
        this.touchdowns = touchdowns;
    }

    public Integer getPointsAllowed() {
        return pointsAllowed;
    }

    public void setPointsAllowed(Integer pointsAllowed) {
        this.pointsAllowed = pointsAllowed;
    }

    public Integer getSaftey() {
        return saftey;
    }

    public void setSaftey(Integer saftey) {
        this.saftey = saftey;
    }
}

