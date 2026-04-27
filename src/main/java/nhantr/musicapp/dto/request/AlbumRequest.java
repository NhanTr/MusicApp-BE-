package nhantr.musicapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "artistId is required")
    private UUID artistId;

    private LocalDate releaseDate;
    private String coverUrl;
}
