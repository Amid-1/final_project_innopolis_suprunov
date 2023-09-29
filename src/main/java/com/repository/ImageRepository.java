package com.repository;

import com.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long>{
    // если нужно вернуть фотографию, которая принадлежит тому или иному юзеру
    Optional<ImageModel> findByUserId(Long userId);
    //найдем фотографию для поста
    Optional<ImageModel> findByPostId(Long postId);
}
