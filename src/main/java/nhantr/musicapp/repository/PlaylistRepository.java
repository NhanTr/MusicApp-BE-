package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Page<Playlist> findByUserId(UUID userId, Pageable pageable);

    Page<Playlist> findByIsPublicTrue(Pageable pageable);
}
