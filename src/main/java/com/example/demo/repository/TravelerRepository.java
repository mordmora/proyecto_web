package com.example.demo.repository;

import com.example.demo.models.Pasajero;
import com.example.demo.models.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TravelerRepository extends JpaRepository<Pasajero, Long> {
    Optional<Pasajero> findByPasaporte(Pasaporte pasaporte);

    List<Pasajero> findByReservasNotEmpty();

    List<Pasajero> findByReservasEmpty();

    long countByReservasNotEmpty();


    List<Pasajero> findAllByOrderByPasaporteIdAsc();

    List<Pasajero> findByPasaporteId(Long pasaporteId);

    @Query("SELECT p FROM Pasajero p JOIN p.reservas r GROUP BY p HAVING COUNT(r) > :cantidad")
    List<Pasajero> findByReservasCountGreaterThan(int cantidad);

    @Query("SELECT p FROM Pasajero p WHERE SIZE(p.reservas) = :cantidad")
    List<Pasajero> findByReservasEquals(int cantidad);

    @Query("SELECT p FROM Pasajero p WHERE p.reservas IS NOT EMPTY ORDER BY p.pasaporte.id ASC")
    List<Pasajero> findTopNWithReservas(int n);

    @Query("SELECT p FROM Pasajero p WHERE p.pasaporte.number LIKE :prefix%")
    List<Pasajero> findByPasaporteNumeroLike(String prefix);
}