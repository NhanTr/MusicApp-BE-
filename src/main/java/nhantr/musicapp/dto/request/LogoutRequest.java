package nhantr.musicapp.dto.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LogoutRequest {
    String refreshToken;
}
