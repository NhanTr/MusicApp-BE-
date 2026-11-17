package nhantr.musicapp.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;
}
