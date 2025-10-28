package rs.fantasy.fantasyfootball.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "player_injuries")
public class PlayerInjury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "injury_id", nullable = false)
    private Injury injury;


    @Column(nullable = false)
    private Integer estimatedRecoveryWeeks;

    @Column(nullable = false)
    private String injuryDate;

    public PlayerInjury() {}

    public PlayerInjury(Player player, Injury injury, Integer estimatedRecoveryWeeks) {
        this.player = player;
        this.injury = injury;
        this.estimatedRecoveryWeeks = estimatedRecoveryWeeks;
    }

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Injury getInjury() { return injury; }
    public void setInjury(Injury injury) { this.injury = injury; }

    public Integer getEstimatedRecoveryWeeks() { return estimatedRecoveryWeeks; }
    public void setEstimatedRecoveryWeeks(Integer estimatedRecoveryWeeks) { this.estimatedRecoveryWeeks = estimatedRecoveryWeeks; }

    public String getInjuryDate() { return injuryDate; }
    public void setInjuryDate(String injuryDate) { this.injuryDate = injuryDate; }

   }
