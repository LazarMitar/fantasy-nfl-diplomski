package rs.fantasy.fantasyfootball.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rs.fantasy.fantasyfootball.model.Message;
import rs.fantasy.fantasyfootball.service.MessageService;

import java.util.Map;

@Controller
public class WebSocketMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public WebSocketMessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String content = payload.get("content").toString();

        // Sačuvaj poruku u bazu
        Message message = messageService.sendMessage(senderId, receiverId, content);

        // Pošalji poruku primaocu preko WebSocket-a
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                message
        );

        // Pošalji potvrdu pošiljaocu
        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                message
        );
    }
}

