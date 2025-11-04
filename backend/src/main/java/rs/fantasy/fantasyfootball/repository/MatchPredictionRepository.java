package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.MatchPrediction;
import rs.fantasy.fantasyfootball.model.RealMatch;
import rs.fantasy.fantasyfootball.model.User;

import java.util.List;
import java.util.Optional;

public interface MatchPredictionRepository extends JpaRepository<MatchPrediction, Long> {

    // Pronađi predikciju korisnika za određeni meč sa eager loading
    @Query("SELECT mp FROM MatchPrediction mp " +
           "LEFT JOIN FETCH mp.realMatch rm " +
           "LEFT JOIN FETCH rm.gameweek " +
           "WHERE mp.user = :user AND mp.realMatch = :realMatch")
    Optional<MatchPrediction> findByUserAndRealMatchWithRelations(@Param("user") User user, @Param("realMatch") RealMatch realMatch);

    // Dohvati sve predikcije korisnika
    @Query("SELECT mp FROM MatchPrediction mp " +
           "LEFT JOIN FETCH mp.realMatch rm " +
           "LEFT JOIN FETCH rm.gameweek " +
           "WHERE mp.user = :user " +
           "ORDER BY rm.gameweek.weekNumber ASC")
    List<MatchPrediction> findByUser(@Param("user") User user);

    // Dohvati sve predikcije za određeni real match
    @Query("SELECT mp FROM MatchPrediction mp " +
           "LEFT JOIN FETCH mp.user " +
           "WHERE mp.realMatch = :realMatch")
    List<MatchPrediction> findByRealMatch(@Param("realMatch") RealMatch realMatch);

    // Proveri da li user već ima predikciju za taj meč
    boolean existsByUserAndRealMatch(User user, RealMatch realMatch);

    // Dohvati sve predikcije za gameweek
    @Query("SELECT mp FROM MatchPrediction mp " +
           "LEFT JOIN FETCH mp.realMatch rm " +
           "WHERE rm.gameweek.id = :gameweekId")
    List<MatchPrediction> findByGameweekId(@Param("gameweekId") Long gameweekId);
}

