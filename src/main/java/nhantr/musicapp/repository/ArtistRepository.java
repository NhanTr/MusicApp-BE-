package nhantr.musicapp.repository;

import java.util.UUID;
import nhantr.musicapp.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {

	@Query(value = """
            SELECT a.*
            FROM artists a
            WHERE unaccent(lower(coalesce(a.name, ''))) LIKE unaccent(lower(concat('%', :query, '%')))
               OR unaccent(lower(coalesce(a.bio, '')))  LIKE unaccent(lower(concat('%', :query, '%')))
            """,
            nativeQuery = true)
    Page<Artist> search(@Param("query") String query, Pageable pageable);
}
