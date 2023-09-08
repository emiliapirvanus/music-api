package com.musicapp.controller;

import com.musicapp.api.generated.ArtistsApi;
import com.musicapp.controller.model.generated.*;
import com.musicapp.repository.ArtistRepository;
import com.musicapp.repository.entities.ArtistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

/**
 * @author Robert Pirvanus
 * <p>
 * 9/8/2023
 */
@Controller
public class ArtistsController implements ArtistsApi {
	@Autowired
	private ArtistRepository artistRepo;

	@Override
	public ResponseEntity<ArtistsGetDto> artistsGet() {
		System.out.println("asdsa");
		return null;
	}

	@Override
	public ResponseEntity<ArtistsGet201Response> artistsPost(ArtistPostDto request) {
		// TODO
		/**
		 *
		 **/

		ArtistEntity artistEntity = convertArtistDtoToEntity(request);

		ArtistEntity savedEntity = artistRepo.save(artistEntity);


		ArtistsGet201Response responseBody = new ArtistsGet201Response();
		responseBody.setArtistId(savedEntity.getId());

		return ResponseEntity.status(201).body(responseBody);
	}

	public ArtistEntity convertArtistDtoToEntity(ArtistPostDto request) {

		ArtistEntity artistEntity = new ArtistEntity();
		artistEntity.setName(request.getName());

		return artistEntity;

	}

	@Override
	public ResponseEntity<ArtistAlbumSongsGetDto> artistsArtistIdAlbumsAlbumIdSongsGet(BigDecimal artistId,
			BigDecimal albumId) {
		return null;
	}

	@Override
	public ResponseEntity<AlbumsGetDto> artistsArtistIdAlbumsGet(Integer artistId) {
		return null;
	}

	@Override
	public ResponseEntity<ArtistDto> artistsArtistIdGet(Integer artistId) {
		return null;
	}

	@Override
	public ResponseEntity<ArtistsGet201Response> artistsArtistIdPatch(Integer artistId, ArtistPostDto artistPostDto) {
		return null;
	}
}
