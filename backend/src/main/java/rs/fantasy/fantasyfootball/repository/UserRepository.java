package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.fantasy.fantasyfootball.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    boolean existsByEmail(String email);
}