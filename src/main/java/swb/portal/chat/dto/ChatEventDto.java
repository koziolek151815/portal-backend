package swb.portal.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatEventDto {

    ChatEventDto.EventType type;

    public enum EventType {
        FOUND, DISCONNECTED
    }
}
