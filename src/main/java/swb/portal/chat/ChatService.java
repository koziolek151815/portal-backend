package swb.portal.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import swb.portal.chat.dto.ChatEventDto;
import swb.portal.chat.dto.ChatMessageDto;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ChatService {
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/chat";
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;


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

    public void startSearchForPartner(ChatPrincipal user)
    {
        synchronized (this) {
            if (!searchQueue.contains(user)) {
                searchQueue.add(user);

                if (searchQueue.size() >= 2) {
                    createChatSession(searchQueue.poll(), searchQueue.poll());
                }
            }
        }
    }

    public void disconnectUser(ChatPrincipal user)
    {
        synchronized (this) {
            searchQueue.remove(user);

            ChatSession currentChatSession = user.getCurrentSession();
            if (currentChatSession != null) {
                removeChatSession(currentChatSession);
            }
        }
    }

    public void sendMessage(ChatPrincipal sender, ChatMessageDto messageDto)
    {
        synchronized (this) {
            ChatSession chatSession = sender.getCurrentSession();

            if (chatSession != null) {
                ChatPrincipal target = chatSession.getOther(sender);

                simpMessagingTemplate.convertAndSendToUser(
                        target.getName(),
                        WS_MESSAGE_TRANSFER_DESTINATION,
                        messageDto);
            }
        }
    }

    public void leaveSession(ChatPrincipal user)
    {
        synchronized (this) {
            ChatSession chatSession = user.getCurrentSession();

            if (chatSession != null) {
                removeChatSession(chatSession);
            }
        }
    }

    public int getUserCount()
    {
        return simpUserRegistry.getUserCount();
    }
}
