package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
}
