package swb.portal.chat;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;


@Setter
@Getter
class ChatPrincipal implements Principal {
    private String name;
    private ChatSession currentSession;

    public ChatPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}