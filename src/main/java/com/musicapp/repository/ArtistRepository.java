package com.musicapp.repository;

import com.musicapp.repository.entities.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Robert Pirvanus
 * <p>
 * 9/8/2023
 */
@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Integer> {
    ArtistEntity findByName(String name);
}
