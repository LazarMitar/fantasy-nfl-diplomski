package rs.fantasy.fantasyfootball.dto;

public class OffenseStatsRequest {
    private Double projectedPoints;
    private Double actualPoints;
    private Integer passingYards;
    private Integer passingTouchdowns;
    private Integer interceptions;
    private Integer rushingYards;
    private Integer rushingTouchdowns;
    private Integer receivingYards;
    private Integer receivingTouchdowns;
    private Integer receivingReceptions;
    private Integer fumbles;

    public OffenseStatsRequest() {}

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

    public Integer getPassingYards() {
        return passingYards;
    }

    public void setPassingYards(Integer passingYards) {
        this.passingYards = passingYards;
    }

    public Integer getPassingTouchdowns() {
        return passingTouchdowns;
    }

    public void setPassingTouchdowns(Integer passingTouchdowns) {
        this.passingTouchdowns = passingTouchdowns;
    }

    public Integer getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(Integer interceptions) {
        this.interceptions = interceptions;
    }

    public Integer getRushingYards() {
        return rushingYards;
    }

    public void setRushingYards(Integer rushingYards) {
        this.rushingYards = rushingYards;
    }

    public Integer getRushingTouchdowns() {
        return rushingTouchdowns;
    }

    public void setRushingTouchdowns(Integer rushingTouchdowns) {
        this.rushingTouchdowns = rushingTouchdowns;
    }

    public Integer getReceivingYards() {
        return receivingYards;
    }

    public void setReceivingYards(Integer receivingYards) {
        this.receivingYards = receivingYards;
    }

    public Integer getReceivingTouchdowns() {
        return receivingTouchdowns;
    }

    public void setReceivingTouchdowns(Integer receivingTouchdowns) {
        this.receivingTouchdowns = receivingTouchdowns;
    }

    public Integer getReceivingReceptions() {
        return receivingReceptions;
    }

    public void setReceivingReceptions(Integer receivingReceptions) {
        this.receivingReceptions = receivingReceptions;
    }

    public Integer getFumbles() {
        return fumbles;
    }

    public void setFumbles(Integer fumbles) {
        this.fumbles = fumbles;
    }
}

