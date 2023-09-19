package com.musicapp.repository.entities;

import com.musicapp.controller.model.generated.ArtistPostDtoAlbumsInner;

import javax.persistence.*;
import java.util.List;

/**
 * @author Robert Pirvanus
 * <p>
 * 9/8/2023
 */
@Entity
@Table(name = "artist")
public class ArtistEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
//	@Column(name = "musicTypes")
	@Column(name = "music_types")
	private String musicTypes;
	@Column(name = "imageUrl")
	private String imageUrl;
//	@Column(name = "albums")
	@Transient
	private List<AlbumEntity> albums;

	public Integer getId() {
		return id;
	}

	public ArtistEntity setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public ArtistEntity setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMusicTypes() {
		return musicTypes;
	}

	public void setMusicTypes(String musicTypes) {
		this.musicTypes = musicTypes;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<AlbumEntity> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumEntity> albums) {
		this.albums = albums;
	}

}
