package nhantr.musicapp.service;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nhantr.musicapp.dto.request.SongRequest;
import nhantr.musicapp.dto.response.PageResponse;
import nhantr.musicapp.dto.response.SongResponse;
import nhantr.musicapp.entity.Album;
import nhantr.musicapp.entity.Artist;
import nhantr.musicapp.entity.Song;
import nhantr.musicapp.enums.ErrorCode;
import nhantr.musicapp.exception.AppException;
import nhantr.musicapp.mapper.MusicMapper;
import nhantr.musicapp.repository.AlbumRepository;
import nhantr.musicapp.repository.ArtistRepository;
import nhantr.musicapp.repository.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.experimental.FieldDefaults;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SongServiceImpl implements SongService {

    final SongRepository songRepository;
    final ArtistRepository artistRepository;
    final AlbumRepository albumRepository;
    final MusicMapper musicMapper;

    @Override
    public PageResponse<SongResponse> getSongs(int page, int size, String sort) {
        log.info("Get songs page={}, size={}, sort={}", page, size, sort);
        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
        Page<SongResponse> responsePage = songRepository.findAll(pageable).map(musicMapper::toSongResponse);
        return PageResponse.fromPage(responsePage);
    }

    @Override
    public SongResponse getSong(UUID id) {
        log.info("Get song detail id={}", id);
        return musicMapper.toSongResponse(getSongEntity(id));
    }

    @Override
    public PageResponse<SongResponse> search(String query, String type, int page, int size) {
        log.info("Search songs query={}, type={}, page={}, size={}", query, type, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Song> songPage = songRepository.search(query == null ? "" : query, pageable);
        return PageResponse.fromPage(songPage.map(musicMapper::toSongResponse));
    }

    @Override
    public PageResponse<SongResponse> getTrending(int page, int size) {
        log.info("Get trending songs page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return PageResponse.fromPage(songRepository.findTrending(pageable).map(musicMapper::toSongResponse));
    }

    @Override
    public SongResponse create(SongRequest request) {
        log.info("Create song title={}, artistId={}", request.getTitle(), request.getArtistId());
        Artist artist = artistRepository
                .findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND.getCode(), ErrorCode.ARTIST_NOT_FOUND.getMessage()));

        Album album = null;
        if (request.getAlbumId() != null) {
            album = albumRepository
                    .findById(request.getAlbumId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(), "Album not found"));
        }

        Song song = Song.builder()
                .title(request.getTitle())
                .artist(artist)
                .album(album)
                .duration(request.getDuration())
                .fileUrl(request.getFileUrl())
                .coverUrl(request.getCoverUrl())
                .createdAt(LocalDateTime.now())
                .build();

        return musicMapper.toSongResponse(songRepository.save(song));
    }

    @Override
    public SongResponse update(UUID id, SongRequest request) {
        log.info("Update song id={}", id);
        Song song = getSongEntity(id);
        Artist artist = artistRepository
                .findById(request.getArtistId())
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND.getCode(), ErrorCode.ARTIST_NOT_FOUND.getMessage()));

        Album album = null;
        if (request.getAlbumId() != null) {
            album = albumRepository
                    .findById(request.getAlbumId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND.getCode(), "Album not found"));
        }

        song.setTitle(request.getTitle());
        song.setArtist(artist);
        song.setAlbum(album);
        song.setDuration(request.getDuration());
        song.setFileUrl(request.getFileUrl());
        song.setCoverUrl(request.getCoverUrl());

        return musicMapper.toSongResponse(songRepository.save(song));
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete song id={}", id);
        songRepository.delete(getSongEntity(id));
    }

    private Song getSongEntity(UUID id) {
        return songRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND.getCode(), ErrorCode.SONG_NOT_FOUND.getMessage()));
    }

    private Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction direction = parts.length > 1 && "asc".equalsIgnoreCase(parts[1])
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(direction, field);
    }
}
