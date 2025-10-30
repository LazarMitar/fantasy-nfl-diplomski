package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "roster_players")
public class RosterPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roster_id", nullable = false)
    private Roster roster;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private boolean starter;

    @Column(nullable = false)
    private boolean captain;

    @Column(nullable = false)
    private LocalDate addedAt;

    public RosterPlayer() {}

    public RosterPlayer(Roster roster, Player player, boolean starter, boolean captain) {
        this.roster = roster;
        this.player = player;
        this.starter = starter;
        this.captain = captain;
        this.addedAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Roster getRoster() {
        return roster;
    }

    public void setRoster(Roster roster) {
        this.roster = roster;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isStarter() {
        return starter;
    }

    public void setStarter(boolean starter) {
        this.starter = starter;
    }

    public LocalDate getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDate addedAt) {
        this.addedAt = addedAt;
    }

    public boolean isCaptain() {
        return captain;
    }
    public void setCaptain(boolean captain) {
        this.captain = captain;
    }
}
