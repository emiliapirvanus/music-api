package com.musicapp.repository;

import com.musicapp.repository.entities.AlbumEntity;
import com.musicapp.repository.entities.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Integer> {

    List<SongEntity> findAllByAlbumId(Integer albumId);
}
