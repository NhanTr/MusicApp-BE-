package nhantr.musicapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.Data;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UploadRequest {

    @NotBlank(message = "title is required")
    String title;

    @NotNull(message = "artistId is required")
    UUID artistId;

    UUID albumId;
    String fileUrl;
    String coverUrl;
}
