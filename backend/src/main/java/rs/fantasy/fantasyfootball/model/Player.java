package rs.fantasy.fantasyfootball.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String team; // npr. "TB", "KC", "BUF"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    private Integer jerseyNumber;

    @Column(nullable = false)
    private Double price;

    public Player() {}

    public Player(String firstName, String lastName, String team, Position position, Integer jerseyNumber, Double price) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.price = price;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
