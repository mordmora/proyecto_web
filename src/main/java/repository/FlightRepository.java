package repository;

import models.Aereolinea;
import models.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FlightRepository extends JpaRepository<Vuelo, Long> {
    Optional<Vuelo> findByUuid(UUID uuid);

    List<Vuelo> findByOrigin(String origin);

    List<Vuelo> findByDestiny(String destiny);

    List<Vuelo> findByOriginAndDestiny(String origin, String destiny);

    List<Vuelo> findByOriginContainingIgnoreCase(String originFragment);

    List<Vuelo> findByDestinyContainingIgnoreCase(String destinyFragment);

    List<Vuelo> findByReservasIsNotEmpty();

    List<Vuelo> findByReservasIsEmpty();

    @Query("SELECT v FROM Vuelo v JOIN v.aereolineas a WHERE a = :aereolinea")
    List<Vuelo> findByAereolinea(Aereolinea aereolinea);


}
