package com.musicapp.repository;

import com.musicapp.repository.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Integer> {
    AlbumEntity findByName(String name);
//    @Query("select * from AlbumEntity where artistId = :artistId")
    List<AlbumEntity> findAllByArtistId(Integer artistId);


}
