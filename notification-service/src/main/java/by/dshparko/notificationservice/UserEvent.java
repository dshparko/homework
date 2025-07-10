package by.dshparko.notificationservice;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserEvent {
    private String type;
    private String email;
}
