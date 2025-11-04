package rs.fantasy.fantasyfootball.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.fantasy.fantasyfootball.model.Message;
import rs.fantasy.fantasyfootball.model.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Dohvatanje svih poruka između dva korisnika
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id_kor = :userId1 AND m.receiver.id_kor = :userId2) OR " +
            "(m.sender.id_kor = :userId2 AND m.receiver.id_kor = :userId1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> getConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    
    // Dohvatanje korisnika kojima sam poslao poruke
    @Query("SELECT DISTINCT m.receiver FROM Message m WHERE m.sender.id_kor = :userId")
    List<User> getReceivers(@Param("userId") Long userId);
    
    // Dohvatanje korisnika od kojih sam primio poruke
    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.id_kor = :userId")
    List<User> getSenders(@Param("userId") Long userId);
    
    // Broj nepročitanih poruka za korisnika
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id_kor = :userId AND m.isRead = false")
    long countUnreadMessages(@Param("userId") Long userId);
    
    // Broj nepročitanih poruka od određenog korisnika
    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id_kor = :receiverId " +
            "AND m.sender.id_kor = :senderId AND m.isRead = false")
    long countUnreadMessagesFrom(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
}

