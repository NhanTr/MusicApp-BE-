package nhantr.musicapp.service;

import java.text.Normalizer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhantr.musicapp.dto.response.AlbumSummaryResponse;
import nhantr.musicapp.dto.response.ArtistSummaryResponse;
import nhantr.musicapp.dto.response.PlaylistResponse;
import nhantr.musicapp.dto.response.SearchResponse;
import nhantr.musicapp.dto.response.SongResponse;
import nhantr.musicapp.mapper.MusicMapper;
import nhantr.musicapp.repository.AlbumRepository;
import nhantr.musicapp.repository.ArtistRepository;
import nhantr.musicapp.repository.PlaylistRepository;
import nhantr.musicapp.repository.SongRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final PlaylistRepository playlistRepository;
    private final MusicMapper musicMapper;

    private String normalize(String s) {
        if (s == null) return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                         .replaceAll("[đĐ]", "d")
                         .toLowerCase()
                         .trim();
    }


    @Override
    public SearchResponse search(String query, int page, int size) {
        log.info("Global search query={}, page={}, size={}", query, page, size);
        String q = query == null ? "" : query;

        List<SongResponse> songs = songRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(s -> normalize(s.getTitle()).contains(normalize(q))
                || normalize(s.getArtist() != null ? s.getArtist().getName() : "").contains(normalize(q)))
                .map(musicMapper::toSongResponse)
                .toList();
        List<ArtistSummaryResponse> artists = artistRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(a -> normalize(a.getName()).contains(normalize(q)))
                .map(a -> ArtistSummaryResponse.builder().id(a.getId()).name(a.getName()).build())
                .toList();
        List<AlbumSummaryResponse> albums = albumRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(a -> normalize(a.getName()).contains(normalize(q)))
                .map(a -> AlbumSummaryResponse.builder().id(a.getId()).name(a.getName()).build())
                .toList();
        List<PlaylistResponse> playlists = playlistRepository.findByIsPublicTrue(PageRequest.of(page, size)).stream()
                .filter(p -> normalize(p.getName()).contains(normalize(q)))
                .map(p -> PlaylistResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .isPublic(p.isPublic())
                        .songCount(0)
                        .build())
                .toList();

        return SearchResponse.builder()
                .songs(songs)
                .artists(artists)
                .albums(albums)
                .playlists(playlists)
                .build();
    }
}
