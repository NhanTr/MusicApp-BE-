package nhantr.musicapp.repository;

import java.util.List;
import java.util.UUID;
import nhantr.musicapp.entity.Favorite;
import nhantr.musicapp.entity.FavoriteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    Page<Favorite> findByUserId(UUID userId, Pageable pageable);

    boolean existsByUserIdAndSongId(UUID userId, UUID songId);

    void deleteByUserIdAndSongId(UUID userId, UUID songId);

    long countBySongId(UUID songId);

    List<Favorite> findBySongId(UUID songId);
}
