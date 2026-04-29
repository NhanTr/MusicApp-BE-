package nhantr.musicapp.repository;

import java.util.List;
import java.util.UUID;
import nhantr.musicapp.entity.PlaylistSong;
import nhantr.musicapp.entity.PlaylistSongId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {

    List<PlaylistSong> findByPlaylistId(UUID playlistId);

    long countByPlaylistId(UUID playlistId);

    boolean existsByPlaylistIdAndSongId(UUID playlistId, UUID songId);

    void deleteByPlaylistIdAndSongId(UUID playlistId, UUID songId);
}
