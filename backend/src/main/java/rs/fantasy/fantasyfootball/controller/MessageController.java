package rs.fantasy.fantasyfootball.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.fantasy.fantasyfootball.model.Message;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.UserRepository;
import rs.fantasy.fantasyfootball.service.MessageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    public MessageController(MessageService messageService, UserRepository userRepository) {
        this.messageService = messageService;
        this.userRepository = userRepository;
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Dohvatanje liste konverzacija za trenutnog korisnika
    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(messageService.getConversations(currentUser.getId_kor()));
    }

    // Dohvatanje konverzacije sa određenim korisnikom
    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable Long otherUserId) {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(messageService.getConversation(currentUser.getId_kor(), otherUserId));
    }

    // Slanje poruke (REST endpoint - takođe ćemo imati WebSocket)
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(
            @RequestParam Long receiverId,
            @RequestParam String content) {
        User currentUser = getCurrentUser();
        Message message = messageService.sendMessage(currentUser.getId_kor(), receiverId, content);
        return ResponseEntity.ok(message);
    }

    // Označavanje poruke kao pročitane
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    // Označavanje cele konverzacije kao pročitane
    @PutMapping("/conversation/{otherUserId}/read")
    public ResponseEntity<Void> markConversationAsRead(@PathVariable Long otherUserId) {
        User currentUser = getCurrentUser();
        messageService.markConversationAsRead(currentUser.getId_kor(), otherUserId);
        return ResponseEntity.ok().build();
    }

    // Broj nepročitanih poruka
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(messageService.getUnreadCount(currentUser.getId_kor()));
    }
    
    // Endpoint za dohvatanje trenutnog korisnika (userId) za WebSocket
    @GetMapping("/current-user-id")
    public ResponseEntity<Long> getCurrentUserId() {
        User currentUser = getCurrentUser();
        return ResponseEntity.ok(currentUser.getId_kor());
    }
    
    // Lista svih korisnika za započinjanje nove konverzacije
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        User currentUser = getCurrentUser();
        List<User> allUsers = userRepository.findAll();
        // Ukloni trenutnog korisnika iz liste
        allUsers.removeIf(u -> u.getId_kor().equals(currentUser.getId_kor()));
        return ResponseEntity.ok(allUsers);
    }
}

