package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "injuries")
public class Injury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String injuryName; // npr. "ACL Tear", "Hamstring", "Ankle Sprain"

    @Column(nullable = false)
    private String estimatedRecovery; // broj nedelja pauze

    public Injury() {}

    public Injury(String injuryName, String estimatedRecoveryWeeks) {
        this.injuryName = injuryName;
        this.estimatedRecovery = estimatedRecoveryWeeks;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInjuryName() { return injuryName; }
    public void setInjuryName(String injuryName) { this.injuryName = injuryName; }

    public String getEstimatedRecoveryWeeks() { return estimatedRecovery; }
    public void setEstimatedRecoveryWeeks(String estimatedRecoveryWeeks) { this.estimatedRecovery = estimatedRecoveryWeeks; }
}
