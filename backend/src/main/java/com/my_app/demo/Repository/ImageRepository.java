package com.my_app.demo.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my_app.demo.entity.ImageEntity;

@Repository //この時点で ImageRepository は Spring 管理下の Bean になる
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByUserId(Long userId);
    List<ImageEntity> findByUserIdAndTagsContaining(Long userId, String keyword, Sort sort);
    List<ImageEntity> findByUserId(Long userId, Sort sort);


    
}
