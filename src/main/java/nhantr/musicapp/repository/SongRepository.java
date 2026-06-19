package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SongRepository extends JpaRepository<Song, UUID> {

    @Query(value = """
            SELECT s.*
            FROM songs s
            LEFT JOIN listening_histories h ON h.song_id = s.id
            GROUP BY s.id
            ORDER BY COUNT(h.id) DESC, s.created_at DESC
            """,
            nativeQuery = true)
    Page<Song> findTrending(Pageable pageable);

    @Query(value = """
    SELECT s.*
    FROM songs s
    LEFT JOIN artists a ON a.id = s.artist_id
    LEFT JOIN albums al ON al.id = s.album_id
    WHERE unaccent(lower(s.title))
        LIKE unaccent(lower(concat('%', :query, '%')))
    OR unaccent(lower(coalesce(a.name, '')))
        LIKE unaccent(lower(concat('%', :query, '%')))
    OR unaccent(lower(coalesce(al.name, '')))
        LIKE unaccent(lower(concat('%', :query, '%')))
    """,
    nativeQuery = true)
    Page<Song> search(@Param("query") String query, Pageable pageable);

    Page<Song> findByArtistId(UUID artistId, Pageable pageable);

    Page<Song> findByAlbumId(UUID albumId, Pageable pageable);
}
