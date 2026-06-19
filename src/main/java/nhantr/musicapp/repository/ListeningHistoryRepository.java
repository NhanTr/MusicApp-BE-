package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.ListeningHistory;
import nhantr.musicapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, UUID> {

    Page<ListeningHistory> findByUserIdOrderByListenedAtDesc(UUID userId, Pageable pageable);

    @Query(value = """
            SELECT COUNT(DISTINCT h.user_id)
            FROM listening_histories h
            WHERE h.song_id = :songId
            """,
            nativeQuery = true)
    long countDistinctUserIdBySongId(@Param("songId") UUID songId);
 
    @Query(value = """
            SELECT h.*
            FROM listening_histories h
            LEFT JOIN songs s  ON s.id  = h.song_id
            LEFT JOIN artists a  ON a.id  = s.artist_id
            LEFT JOIN albums al ON al.id = s.album_id
            WHERE h.user_id = :userId
              AND (
                  unaccent(lower(coalesce(s.title, '')))  LIKE unaccent(lower(concat('%', :query, '%')))
               OR unaccent(lower(coalesce(a.name, '')))   LIKE unaccent(lower(concat('%', :query, '%')))
               OR unaccent(lower(coalesce(al.name, '')))  LIKE unaccent(lower(concat('%', :query, '%')))
              )
            ORDER BY h.listened_at DESC
            """,
            nativeQuery = true)
    Page<ListeningHistory> searchByUserId(@Param("userId") UUID userId, @Param("query") String query, Pageable pageable);
    @Modifying(clearAutomatically = true)
    @Query("delete from ListeningHistory h where h.user = :user")
    void deleteAllByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query("delete from ListeningHistory h where h.song.id = :songId")
    void deleteBySongId(@Param("songId") UUID songId);
}
