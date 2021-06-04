package swb.portal.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChatSession {
    ChatPrincipal firstUser;
    ChatPrincipal secondUser;

    public ChatPrincipal getOther(ChatPrincipal current)
    {
        if(current==firstUser)
            return secondUser;
        else
            return firstUser;
    }
}
