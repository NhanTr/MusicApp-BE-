package nhantr.musicapp.repository;

import java.util.List;
import java.util.UUID;
import nhantr.musicapp.entity.Favorite;
import nhantr.musicapp.entity.FavoriteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    Page<Favorite> findByUserId(UUID userId, Pageable pageable);

        @Query(value = """
            SELECT f.*
            FROM favorites f
            LEFT JOIN songs s  ON s.id  = f.song_id
            LEFT JOIN artists a  ON a.id  = s.artist_id
            LEFT JOIN albums al ON al.id = s.album_id
            WHERE f.user_id = :userId
              AND (
                  unaccent(lower(coalesce(s.title, '')))  LIKE unaccent(lower(concat('%', :query, '%')))
               OR unaccent(lower(coalesce(a.name, '')))   LIKE unaccent(lower(concat('%', :query, '%')))
               OR unaccent(lower(coalesce(al.name, '')))  LIKE unaccent(lower(concat('%', :query, '%')))
              )
            """,
            nativeQuery = true)
    Page<Favorite> searchByUserId(@Param("userId") UUID userId, @Param("query") String query, Pageable pageable);

    boolean existsByUserIdAndSongId(UUID userId, UUID songId);

    void deleteByUserIdAndSongId(UUID userId, UUID songId);

    long countBySongId(UUID songId);

    List<Favorite> findBySongId(UUID songId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Favorite f where f.song.id = :songId")
    void deleteBySongId(@Param("songId") UUID songId);
}
