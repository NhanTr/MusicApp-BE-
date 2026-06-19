package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    Page<Playlist> findByUserId(UUID userId, Pageable pageable);

    Page<Playlist> findByIsPublicTrue(Pageable pageable);

        @Query(value = """
            SELECT p.*
            FROM playlists p
            WHERE p.user_id = :userId
              AND unaccent(lower(coalesce(p.name, ''))) LIKE unaccent(lower(concat('%', :query, '%')))
            """,
            nativeQuery = true)
    Page<Playlist> searchByUserId(@Param("userId") UUID userId, @Param("query") String query, Pageable pageable);
    @Query(value = """
            SELECT p.*
            FROM playlists p
            WHERE p.is_public = true
              AND unaccent(lower(coalesce(p.name, ''))) LIKE unaccent(lower(concat('%', :query, '%')))
            """,
            nativeQuery = true)
    Page<Playlist> searchPublic(@Param("query") String query, Pageable pageable);
}
