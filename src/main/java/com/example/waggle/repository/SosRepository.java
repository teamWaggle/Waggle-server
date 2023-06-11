package com.example.waggle.repository;

import com.example.waggle.domain.Sos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SosRepository extends JpaRepository<Sos, Long> {

    @Query(value = "select s from Sos join fetch s.member m where m.username = :username", nativeQuery = true)
    List<Sos> findByUsername(@Param("username") String username);
}
