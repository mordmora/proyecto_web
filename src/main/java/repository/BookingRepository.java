package repository;

import models.Pasajero;
import models.Reserva;
import models.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Reserva, Long> {
    Optional<Reserva> findByCodigoReserva(UUID codigoReserva);

    List<Reserva> findByPasajero(Pasajero pasajero);

    List<Reserva> findByPasajeroIsNull();

    List<Reserva> findByPasajeroIsNotNull();

    List<Reserva> findByVuelo(Vuelo vuelo);

    List<Reserva> findAllByOrderByCodigoReservaAsc();

    List<Reserva> findAllByOrderByCodigoReservaDesc();

    List<Reserva> findTopNByVueloOrderByCodigoReservaAsc(Vuelo vuelo);

    List<Reserva> findByCodigoReservaContaining(UUID fragment);

    List<Reserva> findByPasajeroAndVuelo(Pasajero pasajero, Vuelo vuelo);

}
