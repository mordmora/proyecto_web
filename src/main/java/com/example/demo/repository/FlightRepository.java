package com.example.demo.repository;

import com.example.demo.models.Aereolinea;
import com.example.demo.models.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Vuelo, Long> {
    Optional<Vuelo> findByUuid(UUID uuid);

    @Query("SELECT v FROM Vuelo v WHERE v.origin = :origin")
    List<Vuelo> findByOrigin(@Param("origin") String origin);

    List<Vuelo> findByDestiny(String destiny);

    @Query("SELECT v FROM Vuelo v WHERE v.origin = :origin AND v.destiny = :destiny")
    List<Vuelo> findByOriginAndDestiny(@Param("origin") String origin, @Param("destiny") String destiny);


    @Query("SELECT v FROM Vuelo v WHERE LOWER(v.origin) LIKE LOWER(CONCAT('%', :fragment, '%'))")
    List<Vuelo> findByOriginContainingIgnoreCase(@Param("fragment") String originFragment);


    List<Vuelo> findByDestinyContainingIgnoreCase(String destinyFragment);

    @Query("SELECT v FROM Vuelo v WHERE SIZE(v.reservas) > 0")
    List<Vuelo> findByReservasIsNotEmpty();


    List<Vuelo> findByReservasIsEmpty();

    @Query("SELECT v FROM Vuelo v JOIN v.aereolineas a WHERE a = :aereolinea")
    List<Vuelo> findByAereolinea(Aereolinea aereolinea);


}
