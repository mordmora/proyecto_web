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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TravelerRepositoryTest {

    @Autowired
    private TravelerRepository travelerRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Pasaporte pasaporte1, pasaporte2, pasaporte3;
    private Pasajero pasajero1, pasajero2, pasajero3;
    private Reserva reserva1;
    private Reserva reserva2;
    private Reserva reserva3;

    @BeforeEach
    void setUp() {
        // Crear pasaportes
        pasaporte1 = new Pasaporte();
        pasaporte1.setNumber("ABC123");
        entityManager.persist(pasaporte1);

        pasaporte2 = new Pasaporte();
        pasaporte2.setNumber("XYZ789");
        entityManager.persist(pasaporte2);

        pasaporte3 = new Pasaporte();
        pasaporte3.setNumber("ABC456");
        entityManager.persist(pasaporte3);

        // Crear pasajeros con colecciones de reservas vacías
        pasajero1 = new Pasajero();
        pasajero1.setPasaporte(pasaporte1);
        pasajero1.setReservas(new HashSet<>());
        entityManager.persist(pasajero1);

        pasajero2 = new Pasajero();
        pasajero2.setPasaporte(pasaporte2);
        pasajero2.setReservas(new HashSet<>());
        entityManager.persist(pasajero2);

        pasajero3 = new Pasajero();
        pasajero3.setPasaporte(pasaporte3);
        pasajero3.setReservas(new HashSet<>());
        entityManager.persist(pasajero3);

        // Crear vuelo
        Vuelo vuelo = new Vuelo();
        vuelo.setOrigin("Madrid");
        vuelo.setDestiny("Barcelona");
        entityManager.persist(vuelo);

        // Usar merge en lugar de persist para las reservas
        reserva1 = new Reserva();
        reserva1.setVuelo(vuelo);
        reserva1.setPasajero(pasajero1);
        reserva1.setCodigoReserva(UUID.fromString("12345678-1234-5678-1234-567812345678"));
        reserva1 = entityManager.merge(reserva1);

        reserva2 = new Reserva();
        reserva2.setVuelo(vuelo);
        reserva2.setPasajero(pasajero1);
        reserva2.setCodigoReserva(UUID.fromString("87654321-4321-8765-4321-876543218765"));
        reserva2 = entityManager.merge(reserva2);


        reserva3 = new Reserva();
        reserva3.setVuelo(vuelo);
        reserva3.setPasajero(pasajero2);
        reserva3.setCodigoReserva(UUID.fromString("11223344-5566-7788-9900-aabbccddeeff"));
        reserva3 = entityManager.merge(reserva3);

        entityManager.flush();
    }

    @Test
    void findByPasaporte() {
        Optional<Pasajero> found = travelerRepository.findByPasaporte(pasaporte1);
        assertTrue(found.isPresent());
        assertEquals(pasajero1.getId(), found.get().getId());
    }

    @Test
    void findByReservasNotEmpty() {
        List<Pasajero> result = travelerRepository.findByReservasNotEmpty();
        assertEquals(2, result.size());
        assertTrue(result.contains(pasajero1));
        assertTrue(result.contains(pasajero2));
        assertFalse(result.contains(pasajero3));
    }

    @Test
    void findByReservasEmpty() {
        List<Pasajero> result = travelerRepository.findByReservasEmpty();
        assertEquals(1, result.size());
        assertTrue(result.contains(pasajero3));
    }

    @Test
    void countByReservasNotEmpty() {
        long count = travelerRepository.countByReservasNotEmpty();
        assertEquals(2, count);
    }

    @Test
    void findAllByOrderByPasaporteIdAsc() {
        List<Pasajero> result = travelerRepository.findAllByOrderByPasaporteIdAsc();
        assertEquals(3, result.size());
        assertEquals(pasaporte1.getId(), result.get(0).getPasaporte().getId());
        assertEquals(pasaporte2.getId(), result.get(1).getPasaporte().getId());
        assertEquals(pasaporte3.getId(), result.get(2).getPasaporte().getId());
    }

    @Test
    void findByPasaporteId() {
        List<Pasajero> result = travelerRepository.findByPasaporteId(pasaporte1.getId());
        assertEquals(1, result.size());
        assertEquals(pasajero1.getId(), result.get(0).getId());
    }

    @Test
    void findByReservasCountGreaterThan() {
        List<Pasajero> result = travelerRepository.findByReservasCountGreaterThan(1);
        assertEquals(1, result.size());
        assertEquals(pasajero1.getId(), result.get(0).getId());
    }

    @Test
    void findByReservasEquals() {
        List<Pasajero> result = travelerRepository.findByReservasEquals(2);
        assertEquals(1, result.size());
        assertEquals(pasajero1.getId(), result.get(0).getId());

        result = travelerRepository.findByReservasEquals(0);
        assertEquals(1, result.size());
        assertEquals(pasajero3.getId(), result.get(0).getId());
    }

    @Test
    void findTopNWithReservas() {
        List<Pasajero> result = travelerRepository.findTopNWithReservas(2);
        assertEquals(2, result.size());
        assertTrue(result.contains(pasajero1));
        assertTrue(result.contains(pasajero2));
    }

    @Test
    void findByPasaporteNumeroLike() {
        List<Pasajero> result = travelerRepository.findByPasaporteNumeroLike("ABC");
        assertEquals(2, result.size());

        result = travelerRepository.findByPasaporteNumeroLike("XYZ");
        assertEquals(1, result.size());
        assertEquals(pasajero2.getId(), result.get(0).getId());
    }
}