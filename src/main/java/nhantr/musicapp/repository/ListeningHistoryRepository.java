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

    @Modifying(clearAutomatically = true)
    @Query("delete from ListeningHistory h where h.user = :user")
    void deleteAllByUser(User user);

    @Modifying(clearAutomatically = true)
    @Query("delete from ListeningHistory h where h.song.id = :songId")
    void deleteBySongId(@Param("songId") UUID songId);
}
