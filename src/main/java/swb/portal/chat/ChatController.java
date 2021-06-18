package swb.portal.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import swb.portal.chat.dto.ChatEventDto;
import swb.portal.chat.dto.ChatMessageDto;
import swb.portal.role.RoleService;
import swb.portal.user.dto.AuthToken;
import swb.portal.user.dto.UserLoginRequestDto;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat.search")
    public void searchForPartner(SimpMessageHeaderAccessor sha) {
        ChatPrincipal user = (ChatPrincipal) sha.getUser();
        chatService.startSearchForPartner(user);
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        ChatPrincipal user = (ChatPrincipal) event.getUser();
        chatService.disconnectUser(user);
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto chatMessage, SimpMessageHeaderAccessor sha) {
        ChatPrincipal sender = (ChatPrincipal) sha.getUser();
        chatService.sendMessage(sender, chatMessage);
    }

    @MessageMapping("/chat.leave")
    public void leaveChat(SimpMessageHeaderAccessor sha) {
        ChatPrincipal user = (ChatPrincipal) sha.getUser();
        chatService.leaveSession(user);
    }

    @RequestMapping(value = "chat/userCount", method = RequestMethod.GET)
    public ResponseEntity<Integer> userCount() throws AuthenticationException {
        return new ResponseEntity<Integer>(chatService.getUserCount(), HttpStatus.OK);
    }

}