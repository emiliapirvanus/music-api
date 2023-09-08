package com.musicapp.repository.entities;

import javax.persistence.*;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

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
}
