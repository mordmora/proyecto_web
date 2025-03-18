package com.example.demo.repository;

import com.example.demo.models.Pasajero;
import com.example.demo.models.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassportRepository extends JpaRepository<Pasaporte, Long> {
    @Query("SELECT p FROM Pasaporte p WHERE p.number = :number")
    List<Pasaporte> findByNumber(@Param("number") String number);

    List<Pasaporte> findByNumberStartingWith(String prefix);

    List<Pasaporte> findByNumberEndingWith(String suffix);

    @Query("SELECT p FROM Pasaporte p WHERE p.number LIKE %:fragment%")
    List<Pasaporte> findByNumberContaining(@Param("fragment") String fragment);

    @Query("SELECT p FROM Pasaporte p ORDER BY p.number ASC")
    List<Pasaporte> findAllByOrderByNumberAsc();

    List<Pasaporte> findAllByOrderByNumberDesc();

    @Query("SELECT p FROM Pasaporte p WHERE p.pasajero = :pasajero")
    Optional<Pasaporte> findByPasajero(@Param("pasajero") Pasajero pasajero);

    @Query("SELECT p FROM Pasaporte p WHERE p.pasajero IS NULL")
    List<Pasaporte> findByPasajeroIsNull();

    @Query("SELECT p FROM Pasaporte p ORDER BY p.number ASC LIMIT 10")
    List<Pasaporte> findTop10ByOrderByNumberAsc();
}