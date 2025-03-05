package repository;

import models.Pasajero;
import models.Pasaporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassportRepository extends JpaRepository<Pasaporte, Long> {
    List<Pasaporte> findByNumber(String number);

    List<Pasaporte> findByNumberStartingWith(String prefix);

    List<Pasaporte> findByNumberEndingWith(String suffix);

    List<Pasaporte> findByNumberContaining(String fragment);

    List<Pasaporte> findAllByOrderByNumberAsc();

    List<Pasaporte> findAllByOrderByNumberDesc();

    Optional<Pasaporte> findByPasajero(Pasajero pasajero);

    List<Pasaporte> findByPasajeroIsNull();

    List<Pasaporte> findTop10ByOrderByNumberAsc();
}
