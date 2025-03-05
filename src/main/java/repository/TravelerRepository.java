package repository;

import models.Pasajero;
import models.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TravelerRepository extends JpaRepository<Pasajero, Long> {
    //Buscar un pasajero por su número de pasaporte
    Optional<Pasajero> findByPasaporte(Pasaporte pasaporte);

    //Buscar pasajeros que tienen al menos una reserva
    List<Pasajero> findByReservasNotEmpty();

    //Buscar pasajeros sin reservas
    List<Pasajero> findByReservasEmpty();

    //Contar cuántos pasajeros tienen al menos una reserva
    long countByReservasNotEmpty();

    //Obtener todos los pasajeros ordenados por ID de pasaporte ascendente
    List<Pasajero> findAllByOrderByPasaporteIdAsc();

    //Buscar pasajeros por ID de pasaporte
    List<Pasajero> findByPasaporteId(Long pasaporteId);

    //Buscar pasajeros que han realizado más de "n" reservas
    @Query("SELECT p FROM Pasajero p JOIN p.reservas r GROUP BY p HAVING COUNT(r) > :cantidad")
    List<Pasajero> findByReservasCountGreaterThan(int cantidad);


    //Obtener los pasajeros con exactamente "n" reservas
    @Query("SELECT p FROM Pasajero p WHERE SIZE(p.reservas) = :cantidad")
    List<Pasajero> findByReservasEquals(int cantidad);

    //Buscar los primeros "n" pasajeros con reservas, ordenados por ID de pasaporte ascendente
    @Query("SELECT p FROM Pasajero p WHERE p.reservas IS NOT EMPTY ORDER BY p.pasaporte.id ASC")
    List<Pasajero> findTopNWithReservas(int n);

    //Buscar pasajeros cuyos números de pasaporte comiencen con un prefijo específico
    @Query("SELECT p FROM Pasajero p WHERE p.pasaporte.number LIKE :prefix%")
    List<Pasajero> findByPasaporteNumeroLike(String prefix);
}
