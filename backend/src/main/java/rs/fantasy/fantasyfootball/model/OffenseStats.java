package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "offense_stats")
@DiscriminatorValue("OFFENSE")
public class OffenseStats extends PlayerGameweekStats {

    private int passingYards;
    private int passingTouchdowns;
    private int interceptions;
    private int rushingYards;
    private int rushingTouchdowns;
    private int receivingYards;
    private int receivingTouchdowns;
    private int receivingReceptions;
    private int fumbles;

    public OffenseStats() {}

    public OffenseStats(Player player, Gameweek gameweek) {
        super(null, player, gameweek, 0.0, 0.0);
    }

    @Override
    public void calculatePoints() {
        double points = 0;
        points += passingYards * 0.04;
        points += passingTouchdowns * 4;
        points -= interceptions * 2;
        points += rushingYards * 0.1;
        points += rushingTouchdowns * 6;
        points += receivingYards * 0.1;
        points += receivingTouchdowns * 6;
        points += receivingReceptions;
        points -= fumbles * 2;

        setActualPoints(points);
    }

    // Getters and Setters
    public int getPassingYards() {
        return passingYards;
    }

    public void setPassingYards(int passingYards) {
        this.passingYards = passingYards;
    }

    public int getPassingTouchdowns() {
        return passingTouchdowns;
    }

    public void setPassingTouchdowns(int passingTouchdowns) {
        this.passingTouchdowns = passingTouchdowns;
    }

    public int getInterceptions() {
        return interceptions;
    }

    public void setInterceptions(int interceptions) {
        this.interceptions = interceptions;
    }

    public int getRushingYards() {
        return rushingYards;
    }

    public void setRushingYards(int rushingYards) {
        this.rushingYards = rushingYards;
    }

    public int getRushingTouchdowns() {
        return rushingTouchdowns;
    }

    public void setRushingTouchdowns(int rushingTouchdowns) {
        this.rushingTouchdowns = rushingTouchdowns;
    }

    public int getReceivingYards() {
        return receivingYards;
    }

    public void setReceivingYards(int receivingYards) {
        this.receivingYards = receivingYards;
    }

    public int getReceivingTouchdowns() {
        return receivingTouchdowns;
    }

    public void setReceivingTouchdowns(int receivingTouchdowns) {
        this.receivingTouchdowns = receivingTouchdowns;
    }

    public int getReceivingReceptions() {
        return receivingReceptions;
    }

    public void setReceivingReceptions(int receivingReceptions) {
        this.receivingReceptions = receivingReceptions;
    }

    public int getFumbles() {
        return fumbles;
    }

    public void setFumbles(int fumbles) {
        this.fumbles = fumbles;
    }
}
