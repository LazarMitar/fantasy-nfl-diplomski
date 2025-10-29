package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String season;

    @Column(nullable = false)
    private Integer numberOfTeams;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id_kor", nullable = false)
    private User createdBy;

    @Column
    private Boolean isAvailable;

    public League() {}

    public League(String name, String season, Integer numberOfTeams, User createdBy) {
        this.name = name;
        this.season = season;
        this.numberOfTeams = numberOfTeams;
        this.createdBy = createdBy;
        this.isAvailable = true;
    }

    // Getteri i setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
