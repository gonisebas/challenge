package com.tenpo.challenge.repository;

import com.tenpo.challenge.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String mail);

    Optional<UserEntity> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update UserEntity set token = :token where id = :id")
    void updateToken(Long id, String token);

    @Transactional
    @Modifying
    @Query("update UserEntity set token = NULL where email = :email")
    int removeTokenByEmail(String email);

}
