package rs.fantasy.fantasyfootball.dto;

public class RegisterRequest {
    private String name;
    private String lastname;
    private String username;
    private String country;
    private String email;
    private String password;
    private String role;
    private String favouriteTeam;

    public RegisterRequest() {}

    public RegisterRequest(String name, String lastName, String username, String country, String favouriteTeam, String email, String password, String role) {
        this.name = name;
        this.lastname = lastName;
        this.username = username;
        this.country = country;
        this.favouriteTeam = favouriteTeam;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFavouriteTeam() {
        return favouriteTeam;
    }

    public void setFavouriteTeam(String favouriteTeam) {
        this.favouriteTeam = favouriteTeam;
    }
}
