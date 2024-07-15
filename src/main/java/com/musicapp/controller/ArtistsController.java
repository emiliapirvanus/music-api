package com.musicapp.controller;

import com.musicapp.api.generated.ArtistsApi;
import com.musicapp.controller.model.generated.*;
import com.musicapp.repository.AlbumRepository;
import com.musicapp.repository.ArtistRepository;
import com.musicapp.repository.SongRepository;
import com.musicapp.repository.entities.AlbumEntity;
import com.musicapp.repository.entities.ArtistEntity;
import com.musicapp.repository.entities.SongEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Robert Pirvanus
 * <p>
 * 9/8/2023
 */
@Controller
public class ArtistsController implements ArtistsApi {
    @Autowired
    private ArtistRepository artistRepo;
    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private SongRepository songRepo;

    @Override
    public ResponseEntity<ArtistsGetDto> artistsGet() {

        List<ArtistEntity> artists = findAllArtistsFromRepo();
        List<ArtistDto> artistDtoList = new ArrayList<>();

        for (ArtistEntity artistEntity : artists) {

            ArtistDto artistDto = convertToDto(artistEntity);
            artistDtoList.add(artistDto);

        }

        ArtistsGetDto responseBody = new ArtistsGetDto();
        responseBody.setArtists(artistDtoList);

        return ResponseEntity.ok(responseBody);
    }

    private List<ArtistEntity> findAllArtistsFromRepo() {

        List<ArtistEntity> artistsEntities = artistRepo.findAll();

        for (ArtistEntity artistsEntity : artistsEntities) {

            List<AlbumEntity> allByArtistId = albumRepo.findAllByArtistId(artistsEntity.getId());

            for (AlbumEntity album : allByArtistId){
                List<SongEntity> songByAlbumId = songRepo.findAllByAlbumId(album.getId());
                album.setSongs(songByAlbumId);
            }

            artistsEntity.setAlbums(allByArtistId);

        }   ////// am adaugat songs

        return artistsEntities;
    }

    @Override
    public ResponseEntity<ArtistsGet201Response> artistsPost(ArtistPostDto request) {

        ArtistEntity existingArtist = artistRepo.findByName(request.getName());
        ArtistEntity artistEntity = convertArtistDtoToEntity(request);

        if (existingArtist != null) {
            artistEntity.setId(existingArtist.getId());
            ArtistEntity savedEntity = artistRepo.save(artistEntity);
            artistEntity.getAlbums().forEach(album -> album.setArtistId(savedEntity.getId()));

            saveAlbumsAndSongs(artistEntity.getAlbums());

            ArtistsGet201Response responseBody = new ArtistsGet201Response();
            responseBody.setArtistId(savedEntity.getId());

            return ResponseEntity.status(201).body(responseBody);

        } else {

            ArtistEntity savedEntity = artistRepo.save(artistEntity);
            artistEntity.getAlbums().forEach(album -> album.setArtistId(savedEntity.getId()));
            saveAlbumsAndSongs(artistEntity.getAlbums());

            ArtistsGet201Response responseBody = new ArtistsGet201Response();
            responseBody.setArtistId(savedEntity.getId());

            return ResponseEntity.status(201).body(responseBody);

        }
    }

    private void saveAlbumsAndSongs(List<AlbumEntity> albums) {

        for (AlbumEntity album : albums) {
            AlbumEntity existingAlbum = albumRepo.findByName(album.getName());
            if (existingAlbum != null) {

                album.setId(existingAlbum.getId());
                AlbumEntity savedAlbum = albumRepo.save(album);
                album.getSongs().forEach(song -> song.setAlbumId(savedAlbum.getId()));
                songRepo.saveAll(album.getSongs());
            } else {

                AlbumEntity savedAlbum = albumRepo.save(album);
                album.getSongs().forEach(song -> song.setAlbumId(savedAlbum.getId()));
                songRepo.saveAll(album.getSongs());
            }
        }
    }

    public ArtistEntity convertArtistDtoToEntity(ArtistPostDto request) {

        ArtistEntity artistEntity = new ArtistEntity();
        List<AlbumEntity> albumEntities = new ArrayList<>();

        artistEntity.setName(request.getName());
        artistEntity.setDescription(request.getDescription());
        artistEntity.setMusicTypes(convertToCommaSeparedString(request.getMusicTypes()));
        artistEntity.setImageUrl(request.getImageUrl());

        for (ArtistPostDtoAlbumsInner album : request.getAlbums()) {
            AlbumEntity albumEntity = convertToAlbumEntity(album);
            albumEntities.add(albumEntity);
        }
        artistEntity.setAlbums(albumEntities);
        return artistEntity;
    }

    private String convertToCommaSeparedString(List<String> musicTypes) {
        if (musicTypes == null) {
            return null;
        }
        return String.join(",", musicTypes);
    }

    private ArtistDto convertToDto(ArtistEntity artistEntity) {

        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(artistEntity.getId());
        artistDto.setName(artistEntity.getName());
        artistDto.setDescription(artistEntity.getDescription());
        artistDto.setImageUrl(artistEntity.getImageUrl());
        artistDto.setMusicTypes(convertCommaSeparedStringToList(artistEntity.getMusicTypes()));

        return artistDto;

    }

    private List<String> convertCommaSeparedStringToList(String musicTypes) {
        if (musicTypes == null) {
            return null;
        }
        String[] types = musicTypes.split(",");
        return Arrays.asList(types);
    }

    private static AlbumEntity convertToAlbumEntity(ArtistPostDtoAlbumsInner album) {
        AlbumEntity albumEntity = new AlbumEntity();
        List<SongEntity> songEntities = new ArrayList<>();

        albumEntity.setName(album.getName());
        albumEntity.setDescription(album.getDescription());

        for (ArtistPostDtoAlbumsInnerSongsInner song : album.getSongs()) {
            SongEntity songEntity = convertSongToEntity(song);
            songEntities.add(songEntity);
        }
        albumEntity.setSongs(songEntities);
        return albumEntity;
    }

    private static SongEntity convertSongToEntity(ArtistPostDtoAlbumsInnerSongsInner song) {
        SongEntity songEntity = new SongEntity();
        songEntity.setName(song.getName());
        songEntity.setMusicType(song.getMusicType());
        songEntity.setUrl(song.getYoutubeUrl());
        return songEntity;
    }

    private static ArtistAlbumSongsGetDtoInner convertSongsToDto (SongEntity songEntity){

        ArtistAlbumSongsGetDtoInner artistAlbumSongsGetDto = new ArtistAlbumSongsGetDtoInner();
        artistAlbumSongsGetDto.setId(songEntity.getId());
        artistAlbumSongsGetDto.setName(songEntity.getName());
        artistAlbumSongsGetDto.setMusicType(songEntity.getMusicType());
        artistAlbumSongsGetDto.setYoutubeUrl(songEntity.getUrl());

        return artistAlbumSongsGetDto;
    }

    @Override
    public ResponseEntity<List<ArtistAlbumSongsGetDtoInner>> artistsArtistIdAlbumsAlbumIdSongsGet(BigDecimal artistId,
                                                                                       BigDecimal albumId) {

        List<SongEntity> allSongsByAlbumId = songRepo.findAllByAlbumId(albumId.intValue());

        List<ArtistAlbumSongsGetDtoInner> albumSongsGetDto = new ArrayList<>();
        for (SongEntity songEntity: allSongsByAlbumId){

            ArtistAlbumSongsGetDtoInner artistAlbumSongsGetDto = convertSongsToDto(songEntity);
            albumSongsGetDto.add(artistAlbumSongsGetDto);
        }
        return ResponseEntity.ok(albumSongsGetDto);
    }

    @Override
    public ResponseEntity<List<AlbumsGetDtoInner>> artistsArtistIdAlbumsGet(Integer artistId) {

        List<AlbumEntity> allByArtistId = albumRepo.findAllByArtistId(artistId);

        for (AlbumEntity album : allByArtistId){
            List<SongEntity> songByAlbumId = songRepo.findAllByAlbumId(album.getId());
            album.setSongs(songByAlbumId);
        }

        List<AlbumsGetDtoInner> albumsGetDtoInners = new ArrayList<>();

        for (AlbumEntity album : allByArtistId) {

            AlbumsGetDtoInner albumsGetDto = convertAlbumsToDto(album);
            albumsGetDtoInners.add(albumsGetDto);
    }

        return  ResponseEntity.ok(albumsGetDtoInners);

    }

    private AlbumsGetDtoInner convertAlbumsToDto(AlbumEntity albumEntity){

        AlbumsGetDtoInner albumsGetDto = new AlbumsGetDtoInner();

        albumsGetDto.setId(albumEntity.getId().toString()); //// dc e string?
        albumsGetDto.setName(albumEntity.getName());
        albumsGetDto.setDescription(albumEntity.getDescription());

        return albumsGetDto;

    }

    @Override
    public ResponseEntity<ArtistDto> artistsArtistIdGet(Integer artistId) {

        List<ArtistEntity> artists = findAllArtistsFromRepo();

        ArtistEntity artist = artists.stream().filter(a -> Objects.equals(a.getId(), artistId)).findFirst().orElse(null);

        if (artist != null){

            return ResponseEntity.ok().body(convertToDto(artist));

        } else {

            return ResponseEntity.notFound().build();

        }
    }

    @Override
    public ResponseEntity<ArtistsGet201Response> artistsArtistIdPatch(Integer artistId, ArtistPostDto artistPostDto) {

        List<ArtistEntity> artists = findAllArtistsFromRepo();
        ArtistEntity artistEntity = convertArtistDtoToEntity(artistPostDto);

        ArtistEntity artist = artists.stream().filter(a -> Objects.equals(a.getId(), artistId)).findFirst().orElse(null);

        if (artist != null){

            artist.setName(artistEntity.getName());
            artist.setDescription(artistEntity.getDescription());
            artist.setImageUrl(artistEntity.getImageUrl());
            artist.setMusicTypes(convertToCommaSeparedString(artistPostDto.getMusicTypes()));
            artist.setAlbums(artistEntity.getAlbums());
        }

        ArtistEntity savedEntity = artistRepo.save(artist);
        artist.getAlbums().forEach(album -> album.setArtistId(savedEntity.getId()));
        saveAlbumsAndSongs(artist.getAlbums());

        ArtistsGet201Response responseBody = new ArtistsGet201Response();
        responseBody.setArtistId(savedEntity.getId());


        return ResponseEntity.status(201).body(responseBody);
    }
}
