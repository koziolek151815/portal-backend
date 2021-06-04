package swb.portal.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import swb.portal.chat.dto.ChatEventDto;
import swb.portal.chat.dto.ChatMessageDto;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/chat";
    private final SimpMessagingTemplate simpMessagingTemplate;


    private Queue<ChatPrincipal> searchQueue = new ArrayDeque<>();
    private Set<ChatSession> chatSessions = new HashSet<>();

    private void createChatSession(ChatPrincipal firstUser, ChatPrincipal secondUser)
    {
        ChatSession chatSession = new ChatSession(firstUser, secondUser);


        chatSessions.add(chatSession);
        firstUser.setCurrentSession(chatSession);
        secondUser.setCurrentSession(chatSession);

        simpMessagingTemplate.convertAndSendToUser(
                firstUser.getName(),
                WS_MESSAGE_TRANSFER_DESTINATION,
                new ChatEventDto(ChatEventDto.EventType.FOUND));

        simpMessagingTemplate.convertAndSendToUser(
                secondUser.getName(),
                WS_MESSAGE_TRANSFER_DESTINATION,
                new ChatEventDto(ChatEventDto.EventType.FOUND));

    }

    private void removeChatSession(ChatSession chatSession)
    {
        ChatPrincipal firstUser = chatSession.getFirstUser();
        ChatPrincipal secondUser = chatSession.getSecondUser();

        chatSessions.remove(chatSession);
        firstUser.setCurrentSession(null);
        secondUser.setCurrentSession(null);

        simpMessagingTemplate.convertAndSendToUser(
                firstUser.getName(),
                WS_MESSAGE_TRANSFER_DESTINATION,
                new ChatEventDto(ChatEventDto.EventType.DISCONNECTED));

        simpMessagingTemplate.convertAndSendToUser(
                secondUser.getName(),
                WS_MESSAGE_TRANSFER_DESTINATION,
                new ChatEventDto(ChatEventDto.EventType.DISCONNECTED));
    }

    @MessageMapping("/chat.search")
    public void searchForPartner(SimpMessageHeaderAccessor sha) {
        ChatPrincipal user = (ChatPrincipal) sha.getUser();

        if(!searchQueue.contains(user)) {
            searchQueue.add(user);

            if (searchQueue.size() >= 2) {
                createChatSession(searchQueue.poll(), searchQueue.poll());
            }
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        ChatPrincipal user = (ChatPrincipal) event.getUser();

        searchQueue.remove(user);

        ChatSession currentChatSession = user.getCurrentSession();
        if(currentChatSession!=null) {
            removeChatSession(currentChatSession);
        }
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto chatMessage, SimpMessageHeaderAccessor sha) {
        ChatPrincipal sender = (ChatPrincipal) sha.getUser();
        ChatSession chatSession = sender.getCurrentSession();

        if(chatSession != null) {
            ChatPrincipal target = chatSession.getOther(sender);

            simpMessagingTemplate.convertAndSendToUser(
                    target.getName(),
                    WS_MESSAGE_TRANSFER_DESTINATION,
                    chatMessage);
        }
    }

    @MessageMapping("/chat.leave")
    public void leaveChat(SimpMessageHeaderAccessor sha) {
        ChatPrincipal sender = (ChatPrincipal) sha.getUser();
        ChatSession chatSession = sender.getCurrentSession();

        if(chatSession != null) {
            removeChatSession(chatSession);
        }
    }
}