package rs.fantasy.fantasyfootball.config;

import rs.fantasy.fantasyfootball.model.*;
import rs.fantasy.fantasyfootball.repository.GameweekRepository;
import rs.fantasy.fantasyfootball.repository.InjuryRepository;
import rs.fantasy.fantasyfootball.repository.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.fantasy.fantasyfootball.repository.RealMatchRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final InjuryRepository injuryRepository;
    private final GameweekRepository gameweekRepository;
    private final RealMatchRepository realMatchRepository;


    public DataSeeder(PlayerRepository playerRepository, InjuryRepository injuryRepository, GameweekRepository  gameweekRepository, RealMatchRepository realMatchRepository) {
        this.playerRepository = playerRepository;
        this.injuryRepository = injuryRepository;
        this.gameweekRepository = gameweekRepository;
        this.realMatchRepository = realMatchRepository;
    }

    @Override
    public void run(String... args) {
        if (playerRepository.count() == 0) {
            List<Player> players = List.of(
                    // Quarterbacks - All 32 NFL Teams
                    new Player("Patrick", "Mahomes", "KC", Position.QB, 15, 12.0),
                    new Player("Josh", "Allen", "BUF", Position.QB, 17, 13.5),
                    new Player("Lamar", "Jackson", "BAL", Position.QB, 8, 13.0),
                    new Player("Jalen", "Hurts", "PHI", Position.QB, 1, 12.5),
                    new Player("Joe", "Burrow", "CIN", Position.QB, 9, 12.0),
                    new Player("C.J.", "Stroud", "HOU", Position.QB, 7, 10.0),
                    new Player("Dak", "Prescott", "DAL", Position.QB, 4, 8.5),
                    new Player("Jordan", "Love", "GB", Position.QB, 10, 10.0),
                    new Player("Brock", "Purdy", "SF", Position.QB, 13, 10.5),
                    new Player("Tua", "Tagovailoa", "MIA", Position.QB, 1, 7.0),
                    new Player("Jared", "Goff", "DET", Position.QB, 16, 10.0),
                    new Player("Matthew", "Stafford", "LAR", Position.QB, 9, 9.5),
                    new Player("Trevor", "Lawrence", "JAX", Position.QB, 16, 8.5),
                    new Player("Geno", "Smith", "LV", Position.QB, 7, 8.0),
                    new Player("Michael", "Penix JR", "ATL", Position.QB, 18, 6.0),
                    new Player("Baker", "Mayfield", "TB", Position.QB, 6, 10.5),
                    new Player("Kyler", "Murray", "ARI", Position.QB, 1, 7.5),
                    new Player("Caleb", "Williams", "CHI", Position.QB, 18, 8.0),
                    new Player("Sam", "Darnold", "SEA", Position.QB, 14, 9.0),
                    new Player("Justin", "Herbert", "LAC", Position.QB, 10, 10.0),
                    new Player("Aaron", "Rodgers", "PIT", Position.QB, 8, 7.5),
                    new Player("Daniel", "Jones", "IND", Position.QB, 8, 6.5),
                    new Player("Jaxon", "Dart", "NYG", Position.QB, 25, 6.5),
                    new Player("Jayden", "Daniels", "WAS", Position.QB, 5, 11.0),
                    new Player("Bo", "Nix", "DEN", Position.QB, 10, 9.0),
                    new Player("Spencer", "Rattler", "NO", Position.QB, 2, 6.0),
                    new Player("Justin", "Fields", "NYJ", Position.QB, 1, 7.5),
                    new Player("Cam", "Ward", "TEN", Position.QB, 1, 6.0),
                    new Player("Bryce", "Young", "CAR", Position.QB, 9, 6.5),
                    new Player("Dillon", "Gabriel", "CLE", Position.QB, 4, 5.5),
                    new Player("JJ", "McCarthy", "MIN", Position.QB, 9, 7.0),
                    new Player("Drake", "Maye", "NE", Position.QB, 10, 9.0),
                // Running Backs
                    // Running Backs - All 32 NFL Teams (2025/26 Season - Corrected)
                    new Player("Saquon", "Barkley", "PHI", Position.RB, 26, 11.5),
                    new Player("Bijan", "Robinson", "ATL", Position.RB, 7, 13.0),
                    new Player("Derrick", "Henry", "BAL", Position.RB, 22, 10.5),
                    new Player("Jahmyr", "Gibbs", "DET", Position.RB, 26, 12.5),
                    new Player("Christian", "McCaffrey", "SF", Position.RB, 23, 13.0),
                    new Player("Josh", "Jacobs", "GB", Position.RB, 8, 11.0),
                    new Player("De'Von", "Achane", "MIA", Position.RB, 28, 11.5),
                    new Player("Omarion", "Hampton", "LAC", Position.RB, 22, 9.5),
                    new Player("Najee", "Harris", "LAC", Position.RB, 22, 5.5),
                    new Player("David", "Montgomery", "DET", Position.RB, 5, 9.5),
                    new Player("Jonathan", "Taylor", "IND", Position.RB, 28, 12.0),
                    new Player("James", "Cook", "BUF", Position.RB, 4, 10.5),
                    new Player("Joe", "Mixon", "HOU", Position.RB, 28, 9.5),
                    new Player("Nick", "Chubb", "HOU", Position.RB, 24, 6.5),
                    new Player("Kenneth", "Walker", "SEA", Position.RB, 9, 9.0),
                    new Player("Kyren", "Williams", "LAR", Position.RB, 23, 10.5),
                    new Player("Breece", "Hall", "NYJ", Position.RB, 20, 9.5),
                    new Player("Alvin", "Kamara", "NO", Position.RB, 41, 9.0),
                    new Player("Ashton", "Jeanty", "LV", Position.RB, 0, 10.0),
                    new Player("J.K.", "Dobbins", "DEN", Position.RB, 27, 8.5),
                    new Player("RJ", "Harvey", "DEN", Position.RB, 23, 7.0),
                    new Player("Rachaad", "White", "TB", Position.RB, 29, 8.5),
                    new Player("Bucky", "Irving", "TB", Position.RB, 7, 10.5),
                    new Player("Travis", "Etienne", "JAX", Position.RB, 1, 8.5),
                    new Player("Tony", "Pollard", "TEN", Position.RB, 20, 8.0),
                    new Player("Quishon", "Judkins", "CLE", Position.RB, 30, 7.5),
                    new Player("Cam", "Skattebo", "NYG", Position.RB, 4, 8.0),
                    new Player("Tyrone", "Tracy", "NYG", Position.RB, 29, 5.0),
                    new Player("Aaron", "Jones", "MIN", Position.RB, 33, 7.5),
                    new Player("Jordan", "Mason", "MIN", Position.RB, 24, 6.5),
                    new Player("D'Andre", "Swift", "CHI", Position.RB, 4, 8.5),
                    new Player("Isiah", "Pacheco", "KC", Position.RB, 10, 7.0),
                    new Player("Bill", "Croskey-Merritt", "WAS", Position.RB, 32, 6.0),
                    new Player("James", "Conner", "ARI", Position.RB, 6, 8.5),
                    new Player("Chuba", "Hubbard", "CAR", Position.RB, 30, 8.0),
                    new Player("Rico", "Dowdle", "CAR", Position.RB, 34, 6.0),
                    new Player("Rhamondre", "Stevenson", "NE", Position.RB, 38, 6.5),
                    new Player("Trey'Von", "Henderson", "NE", Position.RB, 28, 7.5),
                    new Player("Javonte", "Williams", "DAL", Position.RB, 33, 7.0),
                    new Player("Chase", "Brown", "CIN", Position.RB, 30, 10.5),
                    new Player("Jaylen", "Warren", "PIT", Position.RB, 30, 7.5),
                    new Player("Austin", "Ekeler", "WAS", Position.RB, 30, 5.0),
                // Wide Receivers
                    // Wide Receivers - All 32 NFL Teams (2025/26 Season - Corrected)
                    new Player("Ja'Marr", "Chase", "CIN", Position.WR, 1, 13.5),
                    new Player("Justin", "Jefferson", "MIN", Position.WR, 18, 13.5),
                    new Player("A.J.", "Brown", "PHI", Position.WR, 11, 11.0),
                    new Player("CeeDee", "Lamb", "DAL", Position.WR, 88, 11.5),
                    new Player("Amon-Ra", "St. Brown", "DET", Position.WR, 14, 12.0),
                    new Player("Tyreek", "Hill", "MIA", Position.WR, 10, 10.0),
                    new Player("Jaylen", "Waddle", "MIA", Position.WR, 17, 7.5),
                    new Player("DK", "Metcalf", "PIT", Position.WR, 4, 9.5),
                    new Player("Puka", "Nacua", "LAR", Position.WR, 17, 12.5),
                    new Player("Davante", "Adams", "LAR", Position.WR, 17, 10.0),
                    new Player("Garrett", "Wilson", "NYJ", Position.WR, 5, 10.0),
                    new Player("Cooper", "Kupp", "SEA", Position.WR, 10, 7.5),
                    new Player("Malik", "Nabers", "NYG", Position.WR, 1, 11.0),
                    new Player("Brian", "Thomas Jr", "JAX", Position.WR, 7, 10.5),
                    new Player("Travis", "Hunter", "JAX", Position.WR, 12, 7.0),
                    new Player("Jayden", "Reed", "GB", Position.WR, 11, 7.0),
                    new Player("Terry", "McLaurin", "WAS", Position.WR, 17, 9.5),
                    new Player("Deebo", "Samuel", "WAS", Position.WR, 1, 9.5),
                    new Player("Brandon", "Aiyuk", "SF", Position.WR, 11, 8.5),
                    new Player("Ricky", "Pearsall", "SF", Position.WR, 14, 6.0),
                    new Player("Nico", "Collins", "HOU", Position.WR, 12, 11.5),
                    new Player("George", "Pickens", "DAL", Position.WR, 14, 9.0),
                    new Player("DeVonta", "Smith", "PHI", Position.WR, 6, 9.0),
                    new Player("Jaxon", "Smith-Njigba", "SEA", Position.WR, 11, 11.5),
                    new Player("Mike", "Evans", "TB", Position.WR, 13, 10.0),
                    new Player("Chris", "Godwin", "TB", Position.WR, 14, 8.5),
                    new Player("Stefon", "Diggs", "NE", Position.WR, 0, 8.5),
                    new Player("Jordan", "Addison", "MIN", Position.WR, 3, 7.5),
                    new Player("Rome", "Odunze", "CHI", Position.WR, 15, 6.5),
                    new Player("DJ", "Moore", "CHI", Position.WR, 2, 9.0),
                    new Player("Zay", "Flowers", "BAL", Position.WR, 4, 9.5),
                    new Player("Khalil", "Shakir", "BUF", Position.WR, 10, 8.0),
                    new Player("Rashee", "Rice", "KC", Position.WR, 4, 10.5),
                    new Player("Xavier", "Worthy", "KC", Position.WR, 1, 7.5),
                    new Player("Marvin", "Harrison Jr", "ARI", Position.WR, 18, 10.5),
                    new Player("Tee", "Higgins", "CAR", Position.WR, 5, 10.0),
                    new Player("Courtland", "Sutton", "DEN", Position.WR, 14, 9.5),
                    new Player("Michael", "Pittman Jr", "IND", Position.WR, 11, 7.5),
                    new Player("Calvin", "Ridley", "TEN", Position.WR, 0, 8.0),
                    new Player("Quentin", "Johnston", "LAC", Position.WR, 1, 6.0),
                    new Player("Drake", "London", "ATL", Position.WR, 5, 9.5),
                    new Player("Chris", "Olave", "NO", Position.WR, 12, 8.5),
                    new Player("Jakobi", "Meyers", "LV", Position.WR, 16, 7.5),
                    new Player("Keon", "Coleman", "BUF", Position.WR, 0, 6.5),
                    new Player("Matthew", "Golden", "GB", Position.WR, 85, 6.5),
                    new Player("Tank", "Dell", "HOU", Position.WR, 3, 6.0),
                    new Player("Treyton", "McMillan", "CAR", Position.WR, 85, 7.0),
                    new Player("Josh", "Downs", "IND", Position.WR, 1, 5.5),
                    new Player("Ladd", "McConkey", "LAC", Position.WR, 15, 9.0),
                    new Player("Emeka", "Egbuka", "TB", Position.WR, 3, 6.5),
                    new Player("Darnell", "Mooney", "ATL", Position.WR, 1, 5.0),
                    new Player("Jerry", "Jeudy", "CLE", Position.WR, 3, 8.0),
                    new Player("Xavier", "Legette", "CAR", Position.WR, 17, 5.0),

                    // Tight Ends
                    new Player("Dalton", "Kincaid", "BUF", Position.TE, 84, 8.5),
                    new Player("Dawson", "Knox", "BUF", Position.TE, 76, 6.5),
                    new Player("Darren", "Waller", "MIA", Position.TE, 80, 7.0),
                    new Player("Hunter", "Henry", "NE", Position.TE, 80, 7.0),
                    new Player("Mason", "Taylor", "NYJ", Position.TE, 79, 6.0),
                    new Player("Mark", "Andrews", "BAL", Position.TE, 89, 9.0),
                    new Player("Isaiah", "Likely", "BAL", Position.TE, 78, 8.0),
                    new Player("Harold", "Fannin Jr.", "CLE", Position.TE, 81, 5.5),
                    new Player("David", "Njoku", "CLE", Position.TE, 84, 8.5),
                    new Player("Mike", "Gesicki.", "CIN", Position.TE, 88, 7.5),
                    new Player("Pat", "Freiermuth", "PIT", Position.TE, 82, 8.0),
                    new Player("Jonnu", "Smith", "PIT", Position.TE, 78, 7.5),
                    new Player("Dalton", "Schultz", "HOU", Position.TE, 80, 8.0),
                    new Player("Tyler", "Warren", "IND", Position.TE, 82, 10.0),
                    new Player("Evan", "Engram", "DEN", Position.TE, 82, 7.5),
                    new Player("Brenton", "Strange", "JAX", Position.TE, 74, 6.5),
                    new Player("Travis", "Kelce", "KC", Position.TE, 87, 9.0),
                    new Player("Noah", "Gray", "KC", Position.TE, 73, 6.5),
                    new Player("Brock", "Bowers", "LV", Position.TE, 91, 12.0),
                    new Player("Michael", "Mayer", "LV", Position.TE, 75, 7.0),
                    new Player("Oronde", "Gadsden II", "LAC", Position.TE, 88, 7.5),
                    new Player("Chig", "Okonkwo", "TEN", Position.TE, 88, 7.5),
                    new Player("Jake", "Ferguson", "DAL", Position.TE, 83, 10.0),
                    new Player("Dallas", "Goedert", "PHI", Position.TE, 85, 9.5),
                    new Player("Theo", "Johnson", "NYG", Position.TE, 75, 6.5),
                    new Player("Zach", "Ertz", "WAS", Position.TE, 78, 7.0),
                    new Player("Colston", "Loveland", "CHI", Position.TE, 81, 7.0),
                    new Player("Cole", "Kmet", "CHI", Position.TE, 81, 6.5),
                    new Player("Sam", "LaPorta", "DET", Position.TE, 86, 8.5),
                    new Player("Tucker", "Kraft", "GB", Position.TE, 83, 9.5),
                    new Player("Luke", "Musgrave", "GB", Position.TE, 76, 5.0),
                    new Player("T.J.", "Hockenson", "MIN", Position.TE, 87, 9.0),
                    new Player("Josh", "Oliver", "MIN", Position.TE, 72, 5.5),
                    new Player("Kyle", "Pitts", "ATL", Position.TE, 83, 8.5),
                    new Player("Ja'Tavion", "Sanders", "CAR", Position.TE, 77, 5.5),
                    new Player("Juwan", "Johnson", "NO", Position.TE, 74, 6.0),
                    new Player("Cade", "Otton", "TB", Position.TE, 80, 8.0),
                    new Player("Trey", "McBride", "ARI", Position.TE, 88, 11.0),
                    new Player("George", "Kittle", "SF", Position.TE, 90, 10.5),
                    new Player("Tyler", "Higbee", "LAR", Position.TE, 79, 6.0),
                    new Player("Colby", "Parkinson", "LAR", Position.TE, 73, 5.5),
                    new Player("Noah", "Fant", "CIN", Position.TE, 77, 5.5),
                    new Player("AJ", "Barner", "SEA", Position.TE, 88, 7.5),
                // Kickers

                    new Player("Tyler", "Bass", "BUF", Position.K, 20, 6.0),
                    new Player("Jason", "Sanders", "MIA", Position.K, 10, 6.5),
                    new Player("Andres", "Borregales", "NE", Position.K, 37, 5.5),
                    new Player("Nick", "Folk", "NYJ", Position.K, 39, 5.5),
                    new Player("Tyler", "Loop", "BAL", Position.K, 22, 6.5),
                    new Player("Evan", "McPherson", "CIN", Position.K, 2, 7.5),
                    new Player("Dustin", "Hopkins", "CLE", Position.K, 8, 6.0),
                    new Player("Chris", "Boswell", "PIT", Position.K, 9, 8.5),
                    new Player("Ka'imi", "Fairbairn", "HOU", Position.K, 5, 8.0),
                    new Player("Spencer", "Shrader", "IND", Position.K, 24, 7.5),
                    new Player("Cam", "Little", "JAX", Position.K, 42, 6.0),
                    new Player("Joey", "Slye", "TEN", Position.K, 7, 6.0),
                    new Player("Will", "Lutz", "DEN", Position.K, 3, 6.5),
                    new Player("Harrison", "Butker", "KC", Position.K, 7, 8.0),
                    new Player("Daniel", "Carlson", "LV", Position.K, 2, 7.0),
                    new Player("Cameron", "Dicker", "LAC", Position.K, 12, 8.5),
                    new Player("Brandon", "Aubrey", "DAL", Position.K, 1, 9.0),
                    new Player("Graham", "Gano", "NYG", Position.K, 10, 6.5),
                    new Player("Jake", "Elliott", "PHI", Position.K, 6, 7.5),
                    new Player("Matt", "Gay", "WAS", Position.K, 3, 7.0),
                    new Player("Cairo", "Santos", "CHI", Position.K, 5, 6.5),
                    new Player("Jake", "Bates", "DET", Position.K, 30, 7.0),
                    new Player("Brandon", "McManus", "GB", Position.K, 9, 7.5),
                    new Player("Will", "Reichard", "MIN", Position.K, 24, 7.5),
                    new Player("Parker", "Romo", "ATL", Position.K, 6, 6.0),
                    new Player("Matthew", "Wright", "CAR", Position.K, 28, 5.5),
                    new Player("Blake", "Grupe", "NO", Position.K, 23, 6.0),
                    new Player("Chase", "McLaughlin", "TB", Position.K, 4, 8.0),
                    new Player("Chad", "Ryland", "ARI", Position.K, 23, 5.0),
                    new Player("Joshua", "Karty", "LAR", Position.K, 24, 7.0),
                    new Player("Eddy", "Pineiro", "SF", Position.K, 23, 6.0),
                    new Player("Jason", "Myers", "SEA", Position.K, 6, 7.5),
                // Defenses
                    // TOP TIER - Elite odbrane (10+ FPTS/G)
                    new Player("Denver", "Broncos", "DEN", Position.DEF, null, 7.5),
                    new Player("Green Bay", "Packers", "GB", Position.DEF, null, 7.0),
                    new Player("Minnesota", "Vikings", "MIN", Position.DEF, null, 7.0),
                    new Player("Seattle", "Seahawks", "SEA", Position.DEF, null, 7.0),
                    new Player("Cleveland", "Browns", "CLE", Position.DEF, null, 6.5),
                    new Player("Jacksonville", "Jaguars", "JAX", Position.DEF, null, 6.5),
                    new Player("Philadelphia", "Eagles", "PHI", Position.DEF, null, 6.5),
                    new Player("Buffalo", "Bills", "BUF", Position.DEF, null, 6.5),
                    new Player("Tampa Bay", "Buccaneers", "TB", Position.DEF, null, 6.0),
                    new Player("Detroit", "Lions", "DET", Position.DEF, null, 6.0),
                    new Player("San Francisco", "49ers", "SF", Position.DEF, null, 6.0),
                    new Player("Baltimore", "Ravens", "BAL", Position.DEF, null, 5.5),
                    new Player("Los Angeles", "Rams", "LAR", Position.DEF, null, 5.5),
                    new Player("Pittsburgh", "Steelers", "PIT", Position.DEF, null, 5.5),
                    new Player("Kansas City", "Chiefs", "KC", Position.DEF, null, 5.5),
                    new Player("Houston", "Texans", "HOU", Position.DEF, null, 5.0),
                    new Player("Chicago", "Bears", "CHI", Position.DEF, null, 5.0),
                    new Player("Miami", "Dolphins", "MIA", Position.DEF, null, 5.0),
                    new Player("Cincinnati", "Bengals", "CIN", Position.DEF, null, 5.0),
                    new Player("Atlanta", "Falcons", "ATL", Position.DEF, null, 4.5),
                    new Player("Arizona", "Cardinals", "ARI", Position.DEF, null, 4.5),
                    new Player("New England", "Patriots", "NE", Position.DEF, null, 4.5),
                    new Player("Washington", "Commanders", "WAS", Position.DEF, null, 4.5),
                    new Player("New York", "Giants", "NYG", Position.DEF, null, 4.5),
                    new Player("Los Angeles", "Chargers", "LAC", Position.DEF, null, 4.5),
                    new Player("Indianapolis", "Colts", "IND", Position.DEF, null, 4.5),
                    new Player("New Orleans", "Saints", "NO", Position.DEF, null, 4.0),
                    new Player("Dallas", "Cowboys", "DAL", Position.DEF, null, 4.0),
                    new Player("Tennessee", "Titans", "TEN", Position.DEF, null, 4.0),
                    new Player("Carolina", "Panthers", "CAR", Position.DEF, null, 4.0),
                    new Player("Las Vegas", "Raiders", "LV", Position.DEF, null, 4.0),
                    new Player("New York", "Jets", "NYJ", Position.DEF, null, 4.0)
            );
            playerRepository.saveAll(players);
            System.out.println("✅ DataSeeder: Default NFL players successfully inserted!");
        }
        else {
            System.out.println("ℹ️ DataSeeder: Players already exist, skipping seeding.");
        }
        if (injuryRepository.count() == 0)
        {
            List<Injury> injuries = List.of(
                    new Injury("ACL Tear", "godinu dana"),
                    new Injury("MCL Sprain", "4-8 nedelja"),
                    new Injury("Meniscus Tear", "oko 2 meseca"),
                    new Injury("Ankle Sprain", "2-4 nedelje"),
                    new Injury("Hamstring Strain", "3-4 nedelje"),
                    new Injury("Achilles Rupture", "7-8 meseci"),
                    new Injury("Shoulder Dislocation", "5-7 nedelja"),
                    new Injury("AC Joint Sprain", "3-5 nedelja"),
                    new Injury("Biceps Tear", "3-4 meseca"),
                    new Injury("Elbow Hyperextension", "1-3 nedelje"),
                    new Injury("Concussion", "1-2 nedelje"),
                    new Injury("Neck Strain", "1 nedelja"),
                    new Injury("Rib Fracture", "2-4 nedelje"),
                    new Injury("Pectoral Tear", "8-12 nedelja"),
                    new Injury("Back Spasms", "1-2 nedelje"),
                    new Injury("Core Muscle Injury", "6-8 nedelja"),
                    new Injury("Groin Strain", "2-3 nedelje"),
                    new Injury("Turf Toe", "3-5 nedelja"),
                    new Injury("Hand Fracture", "4-6 nedelja"),
                    new Injury("Finger Dislocation", "1-2 nedelje"),
                    new Injury("Wrist Sprain", "2-3 nedelje"),
                    new Injury("Shoulder Labrum Tear", "2-4 meseca"),
                    new Injury("High Ankle Sprain", "5-6 nedelja"),
                    new Injury("Hip Flexor Strain", "3-4 nedelje"),
                    new Injury("Chest Contusion", "1-2 nedelje"),
                    new Injury("Foot Fracture", "6-8 nedelja"),
                    new Injury("Calf Strain", "2-3 nedelje"),
                    new Injury("Quad Contusion", "1-2 nedelje"),
                    new Injury("Oblique Strain", "2-4 nedelje"),
                    new Injury("Knee Contusion", "1-2 nedelje")
            );
            injuryRepository.saveAll(injuries);
            System.out.println("✅ DataSeeder: Default NFL injuries successfully inserted!");
        }
        else {
            System.out.println("ℹ️ DataSeeder: Players already exist, skipping seeding.");
        }

        if (gameweekRepository.count() == 0) {
            String season = "2025-26";
            LocalDateTime firstStart = LocalDateTime.of(2025, 11, 6, 2, 15); // prvi četvrtak 2:15 AM
            LocalDateTime start = firstStart;

            for (int i = 1; i <= 18; i++) {
                LocalDateTime end = start.plusDays(4).withHour(6).withMinute(0);

                Gameweek gw = new Gameweek(i, season, start.truncatedTo(ChronoUnit.MINUTES), end);
                gw.setStatus(GameweekStatus.NOT_STARTED_YET);
                gameweekRepository.save(gw);
                start = start.plusWeeks(1);
            }

            System.out.println("✅ DataSeeder: Default NFL gameweeks successfully inserted!.");
        }
        else
        {
            System.out.println("ℹ️ DataSeeder: Gameweeks already exist, skipping seeding.");

        }
        if (realMatchRepository.count() == 0) {
            Gameweek week1 = gameweekRepository.findByGameweekId(1L);
            if(week1.getId() != null){
                realMatchRepository.save(new RealMatch("DAL", "PHI", week1));
                realMatchRepository.save(new RealMatch("KC", "LAC", week1));
                realMatchRepository.save(new RealMatch("LV", "NE", week1));
                realMatchRepository.save(new RealMatch("PIT", "NYJ", week1));
                realMatchRepository.save(new RealMatch("MIA", "IND", week1));
                realMatchRepository.save(new RealMatch("ARI", "NO", week1));
                realMatchRepository.save(new RealMatch("NYG", "WAS", week1));
                realMatchRepository.save(new RealMatch("CAR", "JAX", week1));
                realMatchRepository.save(new RealMatch("CIN", "CLE", week1));
                realMatchRepository.save(new RealMatch("TB", "ATL", week1));
                realMatchRepository.save(new RealMatch("TEN", "DEN", week1));
                realMatchRepository.save(new RealMatch("SF", "SEA", week1));
                realMatchRepository.save(new RealMatch("DET", "GB", week1));
                realMatchRepository.save(new RealMatch("HOU", "LAR", week1));
                realMatchRepository.save(new RealMatch("BAL", "BUF", week1));
                realMatchRepository.save(new RealMatch("MIN", "CHI", week1));
            }

            Gameweek week2 = gameweekRepository.findByGameweekId(2L);
            if(week2.getId() != null){
                realMatchRepository.save(new RealMatch("WAS", "GB", week2));
                realMatchRepository.save(new RealMatch("JAX", "CIN", week2));
                realMatchRepository.save(new RealMatch("BUF", "NYJ", week2));
                realMatchRepository.save(new RealMatch("NE", "MIA", week2));
                realMatchRepository.save(new RealMatch("LAR", "TEN", week2));
                realMatchRepository.save(new RealMatch("CLE", "BAL", week2));
                realMatchRepository.save(new RealMatch("SF", "NO", week2));
                realMatchRepository.save(new RealMatch("NYG", "DAL", week2));
                realMatchRepository.save(new RealMatch("SEA", "PIT", week2));
                realMatchRepository.save(new RealMatch("CHI", "DET", week2));
                realMatchRepository.save(new RealMatch("DEN", "IND", week2));
                realMatchRepository.save(new RealMatch("CAR", "ARI", week2));
                realMatchRepository.save(new RealMatch("PHI", "KC", week2));
                realMatchRepository.save(new RealMatch("ATL", "MIN", week2));
                realMatchRepository.save(new RealMatch("TB", "HOU", week2));
                realMatchRepository.save(new RealMatch("LAC", "LV", week2));
            }

            Gameweek week3 = gameweekRepository.findByGameweekId(3L);
            if(week3.getId() != null){
                realMatchRepository.save(new RealMatch("MIA", "BUF", week3));
                realMatchRepository.save(new RealMatch("PIT", "NE", week3));
                realMatchRepository.save(new RealMatch("HOU", "JAX", week3));
                realMatchRepository.save(new RealMatch("IND", "TEN", week3));
                realMatchRepository.save(new RealMatch("CIN", "MIN", week3));
                realMatchRepository.save(new RealMatch("NYJ", "TB", week3));
                realMatchRepository.save(new RealMatch("GB", "CLE", week3));
                realMatchRepository.save(new RealMatch("LV", "WAS", week3));
                realMatchRepository.save(new RealMatch("ATL", "CAR", week3));
                realMatchRepository.save(new RealMatch("LAR", "PHI", week3));
                realMatchRepository.save(new RealMatch("NO", "SEA", week3));
                realMatchRepository.save(new RealMatch("DEN", "LAC", week3));
                realMatchRepository.save(new RealMatch("DAL", "CHI", week3));
                realMatchRepository.save(new RealMatch("ARI", "SF", week3));
                realMatchRepository.save(new RealMatch("KC", "NYG", week3));
                realMatchRepository.save(new RealMatch("DET", "BAL", week3));
            }

            Gameweek week4 = gameweekRepository.findByGameweekId(4L);
            if(week4.getId() != null){
                realMatchRepository.save(new RealMatch("SEA", "ARI", week4));
                realMatchRepository.save(new RealMatch("MIN", "PIT", week4)); // Dublin
                realMatchRepository.save(new RealMatch("NO", "BUF", week4));
                realMatchRepository.save(new RealMatch("WAS", "ATL", week4));
                realMatchRepository.save(new RealMatch("LAC", "NYG", week4));
                realMatchRepository.save(new RealMatch("TEN", "HOU", week4));
                realMatchRepository.save(new RealMatch("CLE", "DET", week4));
                realMatchRepository.save(new RealMatch("CAR", "NE", week4));
                realMatchRepository.save(new RealMatch("PHI", "TB", week4));
                realMatchRepository.save(new RealMatch("JAX", "SF", week4));
                realMatchRepository.save(new RealMatch("IND", "LAR", week4));
                realMatchRepository.save(new RealMatch("BAL", "KC", week4));
                realMatchRepository.save(new RealMatch("CHI", "LV", week4));
                realMatchRepository.save(new RealMatch("GB", "DAL", week4));
                realMatchRepository.save(new RealMatch("NYJ", "MIA", week4));
                realMatchRepository.save(new RealMatch("CIN", "DEN", week4));
            }

            Gameweek week5 = gameweekRepository.findByGameweekId(5L);
            if(week5.getId() != null){
                realMatchRepository.save(new RealMatch("SF", "LAR", week5));
                realMatchRepository.save(new RealMatch("MIN", "CLE", week5)); // Tottenham
                realMatchRepository.save(new RealMatch("NYG", "NO", week5));
                realMatchRepository.save(new RealMatch("DEN", "PHI", week5));
                realMatchRepository.save(new RealMatch("HOU", "BAL", week5));
                realMatchRepository.save(new RealMatch("DAL", "NYJ", week5));
                realMatchRepository.save(new RealMatch("LV", "IND", week5));
                realMatchRepository.save(new RealMatch("MIA", "CAR", week5));
                realMatchRepository.save(new RealMatch("TEN", "ARI", week5));
                realMatchRepository.save(new RealMatch("TB", "SEA", week5));
                realMatchRepository.save(new RealMatch("WAS", "LAC", week5));
                realMatchRepository.save(new RealMatch("DET", "CIN", week5));
                realMatchRepository.save(new RealMatch("NE", "BUF", week5));
                realMatchRepository.save(new RealMatch("KC", "JAX", week5));
            }

            Gameweek week6 = gameweekRepository.findByGameweekId(6L);
            if(week6.getId() != null){
                realMatchRepository.save(new RealMatch("PHI", "NYG", week6));
                realMatchRepository.save(new RealMatch("DEN", "NYJ", week6)); // Tottenham
                realMatchRepository.save(new RealMatch("CLE", "PIT", week6));
                realMatchRepository.save(new RealMatch("LAC", "MIA", week6));
                realMatchRepository.save(new RealMatch("SF", "TB", week6));
                realMatchRepository.save(new RealMatch("SEA", "JAX", week6));
                realMatchRepository.save(new RealMatch("DAL", "CAR", week6));
                realMatchRepository.save(new RealMatch("LAR", "BAL", week6));
                realMatchRepository.save(new RealMatch("ARI", "IND", week6));
                realMatchRepository.save(new RealMatch("TEN", "LV", week6));
                realMatchRepository.save(new RealMatch("CIN", "GB", week6));
                realMatchRepository.save(new RealMatch("NE", "NO", week6));
                realMatchRepository.save(new RealMatch("DET", "KC", week6));
                realMatchRepository.save(new RealMatch("CHI", "WAS", week6));
                realMatchRepository.save(new RealMatch("BUF", "ATL", week6));
            }

            Gameweek week7 = gameweekRepository.findByGameweekId(7L);
            if(week7.getId() != null){
                realMatchRepository.save(new RealMatch("PIT", "CIN", week7));
                realMatchRepository.save(new RealMatch("LAR", "JAX", week7)); // Wembley
                realMatchRepository.save(new RealMatch("NE", "TEN", week7));
                realMatchRepository.save(new RealMatch("MIA", "CLE", week7));
                realMatchRepository.save(new RealMatch("LV", "KC", week7));
                realMatchRepository.save(new RealMatch("CAR", "NYJ", week7));
                realMatchRepository.save(new RealMatch("NO", "CHI", week7));
                realMatchRepository.save(new RealMatch("PHI", "MIN", week7));
                realMatchRepository.save(new RealMatch("NYG", "DEN", week7));
                realMatchRepository.save(new RealMatch("IND", "LAC", week7));
                realMatchRepository.save(new RealMatch("WAS", "DAL", week7));
                realMatchRepository.save(new RealMatch("GB", "ARI", week7));
                realMatchRepository.save(new RealMatch("ATL", "SF", week7));
                realMatchRepository.save(new RealMatch("TB", "DET", week7));
                realMatchRepository.save(new RealMatch("HOU", "SEA", week7));
            }

            Gameweek week8 = gameweekRepository.findByGameweekId(8L);
            if(week8.getId() != null){
                realMatchRepository.save(new RealMatch("MIN", "LAC", week8));
                realMatchRepository.save(new RealMatch("NYJ", "CIN", week8));
                realMatchRepository.save(new RealMatch("CHI", "BAL", week8));
                realMatchRepository.save(new RealMatch("MIA", "ATL", week8));
                realMatchRepository.save(new RealMatch("CLE", "NE", week8));
                realMatchRepository.save(new RealMatch("NYG", "PHI", week8));
                realMatchRepository.save(new RealMatch("BUF", "CAR", week8));
                realMatchRepository.save(new RealMatch("SF", "HOU", week8));
                realMatchRepository.save(new RealMatch("TB", "NO", week8));
                realMatchRepository.save(new RealMatch("DAL", "DEN", week8));
                realMatchRepository.save(new RealMatch("TEN", "IND", week8));
                realMatchRepository.save(new RealMatch("GB", "PIT", week8));
                realMatchRepository.save(new RealMatch("WAS", "KC", week8));
            }

            Gameweek week9 = gameweekRepository.findByGameweekId(9L);
            if(week9.getId() != null){
                realMatchRepository.save(new RealMatch("BAL", "MIA", week9));
                realMatchRepository.save(new RealMatch("IND", "PIT", week9));
                realMatchRepository.save(new RealMatch("ATL", "NE", week9));
                realMatchRepository.save(new RealMatch("CHI", "CIN", week9));
                realMatchRepository.save(new RealMatch("LAC", "TEN", week9));
                realMatchRepository.save(new RealMatch("SF", "NYG", week9));
                realMatchRepository.save(new RealMatch("CAR", "GB", week9));
                realMatchRepository.save(new RealMatch("DEN", "HOU", week9));
                realMatchRepository.save(new RealMatch("MIN", "DET", week9));
                realMatchRepository.save(new RealMatch("JAX", "LV", week9));
                realMatchRepository.save(new RealMatch("NO", "LAR", week9));
                realMatchRepository.save(new RealMatch("KC", "BUF", week9));
                realMatchRepository.save(new RealMatch("SEA", "WAS", week9));
                realMatchRepository.save(new RealMatch("ARI", "DAL", week9));
            }

            Gameweek week10 = gameweekRepository.findByGameweekId(10L);
            if(week10.getId() != null){
                realMatchRepository.save(new RealMatch("LV", "DEN", week10));
                realMatchRepository.save(new RealMatch("ATL", "IND", week10)); // Berlin
                realMatchRepository.save(new RealMatch("JAX", "HOU", week10));
                realMatchRepository.save(new RealMatch("BUF", "MIA", week10));
                realMatchRepository.save(new RealMatch("NE", "TB", week10));
                realMatchRepository.save(new RealMatch("CLE", "NYJ", week10));
                realMatchRepository.save(new RealMatch("NYG", "CHI", week10));
                realMatchRepository.save(new RealMatch("NO", "CAR", week10));
                realMatchRepository.save(new RealMatch("BAL", "MIN", week10));
                realMatchRepository.save(new RealMatch("ARI", "SEA", week10));
                realMatchRepository.save(new RealMatch("LAR", "SF", week10));
                realMatchRepository.save(new RealMatch("DET", "WAS", week10));
                realMatchRepository.save(new RealMatch("PIT", "LAC", week10));
                realMatchRepository.save(new RealMatch("PHI", "GB", week10));
            }

            Gameweek week11 = gameweekRepository.findByGameweekId(11L);
            if(week11.getId() != null){
                realMatchRepository.save(new RealMatch("NYJ", "NE", week11));
                realMatchRepository.save(new RealMatch("WAS", "MIA", week11)); // Madrid
                realMatchRepository.save(new RealMatch("TB", "BUF", week11));
                realMatchRepository.save(new RealMatch("LAC", "JAX", week11));
                realMatchRepository.save(new RealMatch("CIN", "PIT", week11));
                realMatchRepository.save(new RealMatch("CAR", "ATL", week11));
                realMatchRepository.save(new RealMatch("GB", "NYG", week11));
                realMatchRepository.save(new RealMatch("CHI", "MIN", week11));
                realMatchRepository.save(new RealMatch("HOU", "TEN", week11));
                realMatchRepository.save(new RealMatch("SF", "ARI", week11));
                realMatchRepository.save(new RealMatch("SEA", "LAR", week11));
                realMatchRepository.save(new RealMatch("KC", "DEN", week11));
                realMatchRepository.save(new RealMatch("BAL", "CLE", week11));
                realMatchRepository.save(new RealMatch("DET", "PHI", week11));
                realMatchRepository.save(new RealMatch("DAL", "LV", week11));
            }

            Gameweek week12 = gameweekRepository.findByGameweekId(12L);
            if(week12.getId() != null){
                realMatchRepository.save(new RealMatch("BUF", "HOU", week12));
                realMatchRepository.save(new RealMatch("NE", "CIN", week12));
                realMatchRepository.save(new RealMatch("PIT", "CHI", week12));
                realMatchRepository.save(new RealMatch("IND", "KC", week12));
                realMatchRepository.save(new RealMatch("NYJ", "BAL", week12));
                realMatchRepository.save(new RealMatch("NYG", "DET", week12));
                realMatchRepository.save(new RealMatch("SEA", "TEN", week12));
                realMatchRepository.save(new RealMatch("MIN", "GB", week12));
                realMatchRepository.save(new RealMatch("CLE", "LV", week12));
                realMatchRepository.save(new RealMatch("JAX", "ARI", week12));
                realMatchRepository.save(new RealMatch("ATL", "NO", week12));
                realMatchRepository.save(new RealMatch("PHI", "DAL", week12));
                realMatchRepository.save(new RealMatch("TB", "LAR", week12));
                realMatchRepository.save(new RealMatch("CAR", "SF", week12));
            }

            Gameweek week13 = gameweekRepository.findByGameweekId(13L);
            if(week13.getId() != null){
                realMatchRepository.save(new RealMatch("GB", "DET", week13));
                realMatchRepository.save(new RealMatch("KC", "DAL", week13));
                realMatchRepository.save(new RealMatch("CIN", "BAL", week13));
                realMatchRepository.save(new RealMatch("CHI", "PHI", week13));
                realMatchRepository.save(new RealMatch("SF", "CLE", week13));
                realMatchRepository.save(new RealMatch("JAX", "TEN", week13));
                realMatchRepository.save(new RealMatch("HOU", "IND", week13));
                realMatchRepository.save(new RealMatch("ARI", "TB", week13));
                realMatchRepository.save(new RealMatch("NO", "MIA", week13));
                realMatchRepository.save(new RealMatch("ATL", "NYJ", week13));
                realMatchRepository.save(new RealMatch("LAR", "CAR", week13));
                realMatchRepository.save(new RealMatch("MIN", "SEA", week13));
                realMatchRepository.save(new RealMatch("BUF", "PIT", week13));
                realMatchRepository.save(new RealMatch("LV", "LAC", week13));
                realMatchRepository.save(new RealMatch("DEN", "WAS", week13));
                realMatchRepository.save(new RealMatch("NYG", "NE", week13));
            }

            Gameweek week14 = gameweekRepository.findByGameweekId(14L);
            if(week14.getId() != null){
                realMatchRepository.save(new RealMatch("DAL", "DET", week14));
                realMatchRepository.save(new RealMatch("IND", "JAX", week14));
                realMatchRepository.save(new RealMatch("NO", "TB", week14));
                realMatchRepository.save(new RealMatch("MIA", "NYJ", week14));
                realMatchRepository.save(new RealMatch("PIT", "BAL", week14));
                realMatchRepository.save(new RealMatch("SEA", "ATL", week14));
                realMatchRepository.save(new RealMatch("TEN", "CLE", week14));
                realMatchRepository.save(new RealMatch("WAS", "MIN", week14));
                realMatchRepository.save(new RealMatch("CHI", "GB", week14));
                realMatchRepository.save(new RealMatch("DEN", "LV", week14));
                realMatchRepository.save(new RealMatch("LAR", "ARI", week14));
                realMatchRepository.save(new RealMatch("CIN", "BUF", week14));
                realMatchRepository.save(new RealMatch("HOU", "KC", week14));
                realMatchRepository.save(new RealMatch("PHI", "LAC", week14));
            }

            Gameweek week15 = gameweekRepository.findByGameweekId(15L);
            if(week15.getId() != null){
                realMatchRepository.save(new RealMatch("ATL", "TB", week15));
                realMatchRepository.save(new RealMatch("LAC", "KC", week15));
                realMatchRepository.save(new RealMatch("BUF", "NE", week15));
                realMatchRepository.save(new RealMatch("NYJ", "JAX", week15));
                realMatchRepository.save(new RealMatch("BAL", "CIN", week15));
                realMatchRepository.save(new RealMatch("LV", "PHI", week15));
                realMatchRepository.save(new RealMatch("ARI", "HOU", week15));
                realMatchRepository.save(new RealMatch("WAS", "NYG", week15));
                realMatchRepository.save(new RealMatch("CLE", "CHI", week15));
                realMatchRepository.save(new RealMatch("DET", "LAR", week15));
                realMatchRepository.save(new RealMatch("TEN", "SF", week15));
                realMatchRepository.save(new RealMatch("CAR", "NO", week15));
                realMatchRepository.save(new RealMatch("GB", "DEN", week15));
                realMatchRepository.save(new RealMatch("IND", "SEA", week15));
                realMatchRepository.save(new RealMatch("MIN", "DAL", week15));
                realMatchRepository.save(new RealMatch("MIA", "PIT", week15));
            }

            Gameweek week16 = gameweekRepository.findByGameweekId(16L);
            if(week16.getId() != null){
                realMatchRepository.save(new RealMatch("LAR", "SEA", week16));
                realMatchRepository.save(new RealMatch("GB", "CHI", week16));
                realMatchRepository.save(new RealMatch("PHI", "WAS", week16));
                realMatchRepository.save(new RealMatch("KC", "TEN", week16));
                realMatchRepository.save(new RealMatch("NYJ", "NO", week16));
                realMatchRepository.save(new RealMatch("NE", "BAL", week16));
                realMatchRepository.save(new RealMatch("BUF", "CLE", week16));
                realMatchRepository.save(new RealMatch("TB", "CAR", week16));
                realMatchRepository.save(new RealMatch("MIN", "NYG", week16));
                realMatchRepository.save(new RealMatch("LAC", "DAL", week16));
                realMatchRepository.save(new RealMatch("ATL", "ARI", week16));
                realMatchRepository.save(new RealMatch("JAX", "DEN", week16));
                realMatchRepository.save(new RealMatch("PIT", "DET", week16));
                realMatchRepository.save(new RealMatch("LV", "HOU", week16));
                realMatchRepository.save(new RealMatch("CIN", "MIA", week16));
                realMatchRepository.save(new RealMatch("SF", "IND", week16));
            }

            Gameweek week17 = gameweekRepository.findByGameweekId(17L);
            if(week17.getId() != null){
                realMatchRepository.save(new RealMatch("DAL", "WAS", week17)); // Christmas Netflix
                realMatchRepository.save(new RealMatch("DET", "MIN", week17)); // Christmas Netflix
                realMatchRepository.save(new RealMatch("DEN", "KC", week17)); // Christmas Prime
                realMatchRepository.save(new RealMatch("NYG", "LV", week17));
                realMatchRepository.save(new RealMatch("HOU", "LAC", week17));
                realMatchRepository.save(new RealMatch("ARI", "CIN", week17));
                realMatchRepository.save(new RealMatch("BAL", "GB", week17));
                realMatchRepository.save(new RealMatch("SEA", "CAR", week17));
                realMatchRepository.save(new RealMatch("NO", "TEN", week17));
                realMatchRepository.save(new RealMatch("PIT", "CLE", week17));
                realMatchRepository.save(new RealMatch("NE", "NYJ", week17));
                realMatchRepository.save(new RealMatch("JAX", "IND", week17));
                realMatchRepository.save(new RealMatch("TB", "MIA", week17));
                realMatchRepository.save(new RealMatch("PHI", "BUF", week17));
                realMatchRepository.save(new RealMatch("CHI", "SF", week17));
                realMatchRepository.save(new RealMatch("LAR", "ATL", week17));
            }

            Gameweek week18 = gameweekRepository.findByGameweekId(18L);
            if(week18.getId() != null){
                realMatchRepository.save(new RealMatch("NYJ", "BUF", week18));
                realMatchRepository.save(new RealMatch("KC", "LAC", week18));
                realMatchRepository.save(new RealMatch("BAL", "PIT", week18));
                realMatchRepository.save(new RealMatch("CLE", "CIN", week18));
                realMatchRepository.save(new RealMatch("MIA", "NE", week18));
                realMatchRepository.save(new RealMatch("TEN", "JAX", week18));
                realMatchRepository.save(new RealMatch("LAC", "DEN", week18));
                realMatchRepository.save(new RealMatch("IND", "HOU", week18));
                realMatchRepository.save(new RealMatch("DET", "CHI", week18));
                realMatchRepository.save(new RealMatch("GB", "MIN", week18));
                realMatchRepository.save(new RealMatch("NO", "ATL", week18));
                realMatchRepository.save(new RealMatch("SEA", "SF", week18));
                realMatchRepository.save(new RealMatch("WAS", "PHI", week18));
                realMatchRepository.save(new RealMatch("DAL", "NYG", week18));
                realMatchRepository.save(new RealMatch("CAR", "TB", week18));
                realMatchRepository.save(new RealMatch("ARI", "LAR", week18));
            }
        }
        else
        {
            System.out.println("ℹ️ DataSeeder: Real matches already exist, skipping seeding.");
        }
    }
}

