package rs.fantasy.fantasyfootball.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.fantasy.fantasyfootball.model.Message;
import rs.fantasy.fantasyfootball.model.User;
import rs.fantasy.fantasyfootball.repository.MessageRepository;
import rs.fantasy.fantasyfootball.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message(sender, receiver, content);
        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long userId1, Long userId2) {
        return messageRepository.getConversation(userId1, userId2);
    }

    public List<Map<String, Object>> getConversations(Long userId) {
        // Dohvati korisnike od kojih sam primio poruke
        List<User> senders = messageRepository.getSenders(userId);
        
        // Dohvati korisnike kojima sam poslao poruke
        List<User> receivers = messageRepository.getReceivers(userId);
        
        // Kombinuj liste i ukloni duplikate koristeći Set
        Set<User> allPartners = new HashSet<>();
        allPartners.addAll(senders);
        allPartners.addAll(receivers);
        
        return allPartners.stream()
                .map(user -> {
                    Map<String, Object> conv = new HashMap<>();
                    conv.put("userId", user.getId_kor());
                    conv.put("username", user.getUsername());
                    conv.put("name", user.getName());
                    conv.put("lastname", user.getLastname());
                    conv.put("unreadCount", messageRepository.countUnreadMessagesFrom(userId, user.getId_kor()));
                    return conv;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        messageRepository.save(message);
    }

    @Transactional
    public void markConversationAsRead(Long currentUserId, Long otherUserId) {
        List<Message> messages = messageRepository.getConversation(currentUserId, otherUserId);
        messages.stream()
                .filter(m -> m.getReceiver().getId_kor().equals(currentUserId) && !m.isRead())
                .forEach(m -> {
                    m.setRead(true);
                    messageRepository.save(m);
                });
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countUnreadMessages(userId);
    }
}

