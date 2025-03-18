// Archivo: src/test/java/com/example/demo/repository/BookingRepositoryTest.java
package com.example.demo.repository;

import com.example.demo.models.Pasajero;
import com.example.demo.models.Pasaporte;
import com.example.demo.models.Reserva;
import com.example.demo.models.Vuelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Pasajero pasajeroConReserva;
    private Pasajero pasajeroSinReserva;
    private Vuelo vuelo;
    private Reserva reserva1;
    private Reserva reserva2;
    private Reserva reservaSinPasajero;
    private UUID codigo1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private UUID codigo2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private UUID codigo3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

    @BeforeEach
    void setUp() {
        // Crear Pasaporte y Pasajero para pasajeroConReserva
        Pasaporte pasaporte1 = new Pasaporte();
        pasaporte1.setNumber("NUM001");
        entityManager.persistAndFlush(pasaporte1);

        pasajeroConReserva = new Pasajero();
        pasajeroConReserva.setPasaporte(pasaporte1);
        pasajeroConReserva.setReservas(new HashSet<>());
        entityManager.persistAndFlush(pasajeroConReserva);

        // Crear Pasaporte y Pasajero sin reservas
        Pasaporte pasaporte2 = new Pasaporte();
        pasaporte2.setNumber("NUM002");
        entityManager.persistAndFlush(pasaporte2);

        pasajeroSinReserva = new Pasajero();
        pasajeroSinReserva.setPasaporte(pasaporte2);
        pasajeroSinReserva.setReservas(new HashSet<>());
        entityManager.persistAndFlush(pasajeroSinReserva);

        // Crear Vuelo
        vuelo = new Vuelo();
        vuelo.setOrigin("Madrid");
        vuelo.setDestiny("Barcelona");
        vuelo = entityManager.persistAndFlush(vuelo);

        // Crear Reserva con pasajeroConReserva y codigo1
        reserva1 = new Reserva();
        reserva1.setCodigoReserva(codigo1);
        reserva1.setPasajero(pasajeroConReserva);
        reserva1.setVuelo(vuelo);
        reserva1 = entityManager.merge(reserva1);

        // Agregar reserva al pasajeroConReserva
        pasajeroConReserva.getReservas().add(reserva1);
        entityManager.persistAndFlush(pasajeroConReserva);

        // Crear otra Reserva para el mismo pasajero y vuelo con codigo2
        reserva2 = new Reserva();
        reserva2.setCodigoReserva(codigo2);
        reserva2.setPasajero(pasajeroConReserva);
        reserva2.setVuelo(vuelo);
        reserva2 = entityManager.merge(reserva2);

        pasajeroConReserva.getReservas().add(reserva2);
        entityManager.persistAndFlush(pasajeroConReserva);

        // Crear Reserva sin pasajero con codigo3
        reservaSinPasajero = new Reserva();
        reservaSinPasajero.setCodigoReserva(codigo3);
        reservaSinPasajero.setPasajero(null);
        reservaSinPasajero.setVuelo(vuelo);
        reservaSinPasajero = entityManager.merge(reservaSinPasajero);

        entityManager.flush();
    }

    @Test
    void findByCodigoReserva() {
        Optional<Reserva> found = bookingRepository.findByCodigoReserva(codigo1);
        assertTrue(found.isPresent());
        assertEquals(codigo1, found.get().getCodigoReserva());
    }

    @Test
    void findByPasajero() {
        List<Reserva> reservas = bookingRepository.findByPasajero(pasajeroConReserva);
        assertNotNull(reservas);
        // Se esperan dos reservas para pasajeroConReserva
        assertEquals(2, reservas.size());
    }

    @Test
    void findByPasajeroIsNull() {
        List<Reserva> reservas = bookingRepository.findByPasajeroIsNull();
        assertNotNull(reservas);
        // Solo la reservaSinPasajero no tiene pasajero
        assertEquals(1, reservas.size());
        assertNull(reservas.get(0).getPasajero());
    }

    @Test
    void findByPasajeroIsNotNull() {
        List<Reserva> reservas = bookingRepository.findByPasajeroIsNotNull();
        assertNotNull(reservas);
        // Las dos reservas con pasajero
        assertEquals(2, reservas.size());
        reservas.forEach(r -> assertNotNull(r.getPasajero()));
    }

    @Test
    void findByVuelo() {
        List<Reserva> reservas = bookingRepository.findByVuelo(vuelo);
        assertNotNull(reservas);
        // Hay tres reservas asociadas al mismo vuelo
        assertEquals(3, reservas.size());
    }

    @Test
    void findAllByOrderByCodigoReservaAsc() {
        List<Reserva> reservas = bookingRepository.findAllByOrderByCodigoReservaAsc();
        assertNotNull(reservas);
        assertEquals(3, reservas.size());
        // Verifica orden ascendente comparando códigos
        assertTrue(reservas.get(0).getCodigoReserva().toString()
                .compareTo(reservas.get(1).getCodigoReserva().toString()) <= 0);
    }

    @Test
    void findAllByOrderByCodigoReservaDesc() {
        List<Reserva> reservas = bookingRepository.findAllByOrderByCodigoReservaDesc();
        assertNotNull(reservas);
        assertEquals(3, reservas.size());
        // Verifica orden descendente comparando códigos
        assertTrue(reservas.get(0).getCodigoReserva().toString()
                .compareTo(reservas.get(1).getCodigoReserva().toString()) >= 0);
    }

    @Test
    void findTop10ByVueloOrderByCodigoReservaAsc() {
        List<Reserva> reservas = bookingRepository.findTop10ByVueloOrderByCodigoReservaAsc(vuelo);
        assertNotNull(reservas);
        // Como solo hay tres reservas se esperan 3
        assertEquals(3, reservas.size());
        // Verifica que estén ordenadas ascendentemente
        for (int i = 0; i < reservas.size() - 1; i++) {
            assertTrue(reservas.get(i).getCodigoReserva().toString()
                    .compareTo(reservas.get(i+1).getCodigoReserva().toString()) <= 0);
        }
    }

    @Test
    void findByCodigoReservaContaining() {
        // Usar fragmento de código: se espera encontrar la reserva cuyo código contenga "1111"
        List<Reserva> reservas = bookingRepository.findByCodigoReservaContaining("1111");
        assertNotNull(reservas);

        assertEquals(1, reservas.size());
        assertEquals(codigo1, reservas.get(0).getCodigoReserva());
    }

    @Test
    void findByPasajeroAndVuelo() {
        List<Reserva> reservas = bookingRepository.findByPasajeroAndVuelo(pasajeroConReserva, vuelo);
        assertNotNull(reservas);
        // Se esperan dos reservas para la combinación pasajeroConReserva y vuelo
        assertEquals(2, reservas.size());
    }
}