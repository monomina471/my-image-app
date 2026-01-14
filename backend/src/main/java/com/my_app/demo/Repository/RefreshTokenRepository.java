package com.my_app.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.my_app.demo.entity.RefreshToken;
import com.my_app.demo.entity.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);

    @Modifying // データを変更（削除）する場合に必要
    @Transactional // 削除にはトランザクションが必要
    void deleteByUser(UserEntity user);
} 
