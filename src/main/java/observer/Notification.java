package observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import observer.enums.NotificationCode;

@Data
@AllArgsConstructor
public class Notification {
    private NotificationCode code;
    private Object data;
}
