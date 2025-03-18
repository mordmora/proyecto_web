package com.example.demo.repository;

import com.example.demo.models.Pasajero;
import com.example.demo.models.Reserva;
import com.example.demo.models.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.codigoReserva = :codigo")
    Optional<Reserva> findByCodigoReserva(@Param("codigo") UUID codigoReserva);

    @Query("SELECT r FROM Reserva r WHERE r.pasajero = :pasajero")
    List<Reserva> findByPasajero(@Param("pasajero") Pasajero pasajero);

    // Consulta 3: JPQL para reservas sin pasajero
    @Query("SELECT r FROM Reserva r WHERE r.pasajero IS NULL")
    List<Reserva> findByPasajeroIsNull();
    List<Reserva> findByPasajeroIsNotNull();
    List<Reserva> findByVuelo(Vuelo vuelo);

    @Query("SELECT r FROM Reserva r ORDER BY r.codigoReserva ASC")
    List<Reserva> findAllByOrderByCodigoReservaAsc();

    List<Reserva> findAllByOrderByCodigoReservaDesc();
    List<Reserva> findTop10ByVueloOrderByCodigoReservaAsc(Vuelo vuelo);

    @Query("SELECT r FROM Reserva r WHERE CAST(r.codigoReserva AS string) LIKE %:codigo%")
    List<Reserva> findByCodigoReservaContaining(@Param("codigo") String codigo);

    @Query("SELECT r FROM Reserva r WHERE r.pasajero = :pasajero AND r.vuelo = :vuelo")
    List<Reserva> findByPasajeroAndVuelo(@Param("pasajero") Pasajero pasajero, @Param("vuelo") Vuelo vuelo);
}