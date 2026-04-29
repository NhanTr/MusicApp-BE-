package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.ListeningHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, UUID> {

    Page<ListeningHistory> findByUserIdOrderByListenedAtDesc(UUID userId, Pageable pageable);

    void deleteByUserId(UUID userId);
}
