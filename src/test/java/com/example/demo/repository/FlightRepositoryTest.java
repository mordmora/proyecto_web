// Archivo: src/test/java/com/example/demo/repository/FlightRepositoryTest.java
package com.example.demo.repository;

import com.example.demo.models.Aereolinea;
import com.example.demo.models.Reserva;
import com.example.demo.models.Pasajero;
import com.example.demo.models.Pasaporte;
import com.example.demo.models.Vuelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Vuelo vuelo1;
    private Vuelo vuelo2;
    private Aereolinea aerolinea;
    private Reserva reserva;
    private Pasajero pasajero;
    private Pasaporte pasaporte;

    @BeforeEach
    void setUp() {
        // Crear vuelos
        vuelo1 = new Vuelo();
        vuelo1.setUuid(UUID.randomUUID());
        vuelo1.setOrigin("Madrid");
        vuelo1.setDestiny("Barcelona");
        vuelo1 = entityManager.persistAndFlush(vuelo1);

        vuelo2 = new Vuelo();
        vuelo2.setUuid(UUID.randomUUID());
        vuelo2.setOrigin("Valencia");
        vuelo2.setDestiny("Sevilla");
        vuelo2 = entityManager.persistAndFlush(vuelo2);

        Vuelo vuelo3 = new Vuelo();
        vuelo3.setUuid(UUID.randomUUID());
        vuelo3.setOrigin("mAdRid");
        vuelo3.setDestiny("Bilbao");
        vuelo3 = entityManager.persistAndFlush(vuelo3);

        // Crear reserva para vuelo1 (para probar reservas no vacías)
        pasaporte = new Pasaporte();
        pasaporte.setNumber("TEST100");
        pasaporte = entityManager.persistAndFlush(pasaporte);

        pasajero = new Pasajero();
        pasajero.setPasaporte(pasaporte);
        pasajero.setReservas(new HashSet<>());
        pasajero = entityManager.persistAndFlush(pasajero);

        reserva = new Reserva();
        reserva.setVuelo(vuelo1);
        reserva.setPasajero(pasajero);
        reserva.setCodigoReserva(UUID.randomUUID());
        reserva = entityManager.merge(reserva);
        // Actualizar la colección de reservas en vuelo1
        vuelo1.setReservas(Set.of(reserva));
        entityManager.persistAndFlush(vuelo1);

        // Crear aerolínea y asociarle vuelo2
        aerolinea = new Aereolinea();
        aerolinea.setName("AirTest");
        aerolinea.getVuelos().add(vuelo2);
        aerolinea = entityManager.persistAndFlush(aerolinea);
    }

    @Test
    void findByUuid() {
        Optional<Vuelo> found = flightRepository.findByUuid(vuelo1.getUuid());
        assertTrue(found.isPresent());
        assertEquals(vuelo1.getId(), found.get().getId());
    }

    @Test
    void findByOrigin() {
        List<Vuelo> result = flightRepository.findByOrigin("Madrid");
        assertNotNull(result);
        // Se espera encontrar al menos vuelo1 con origin "Madrid" (sensitivo)
        assertFalse(result.isEmpty());
    }

    @Test
    void findByDestiny() {
        List<Vuelo> result = flightRepository.findByDestiny("Barcelona");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Barcelona", result.get(0).getDestiny());
    }

    @Test
    void findByOriginAndDestiny() {
        List<Vuelo> result = flightRepository.findByOriginAndDestiny("Madrid", "Barcelona");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vuelo1.getId(), result.get(0).getId());
    }

    @Test
    void findByOriginContainingIgnoreCase() {
        // Buscamos fragmento en origin sin distinguir mayúsculas
        List<Vuelo> result = flightRepository.findByOriginContainingIgnoreCase("mad");
        assertNotNull(result);
        // Se esperan vuelos con "Madrid" en origin (vuelo1 y vuelo3)
        assertTrue(result.size() >= 2);
    }

    @Test
    void findByDestinyContainingIgnoreCase() {
        List<Vuelo> result = flightRepository.findByDestinyContainingIgnoreCase("bar");
        assertNotNull(result);
        // "Barcelona" contiene "bar" ignorando mayúsculas
        assertEquals(1, result.size());
        assertEquals("Barcelona", result.get(0).getDestiny());
    }

    @Test
    void findByReservasIsNotEmpty() {
        List<Vuelo> result = flightRepository.findByReservasIsNotEmpty();
        assertNotNull(result);
        // Solo vuelo1 tiene reservas
        assertEquals(1, result.size());
        assertEquals(vuelo1.getId(), result.get(0).getId());
    }

    @Test
    void findByReservasIsEmpty() {
        List<Vuelo> result = flightRepository.findByReservasIsEmpty();
        assertNotNull(result);
        // vuelos sin reservas: vuelo2 y vuelo3
        assertTrue(result.size() >= 2);
    }

    @Test
    void findByAereolinea() {
        List<Vuelo> result = flightRepository.findByAereolinea(aerolinea);
        assertNotNull(result);
        // Solo vuelo2 está asociado a la aerolínea creada
        assertEquals(1, result.size());
        assertEquals(vuelo2.getId(), result.get(0).getId());
    }
}