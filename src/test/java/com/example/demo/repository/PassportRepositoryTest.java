// Archivo: src/test/java/com/example/demo/repository/PassportRepositoryTest.java
package com.example.demo.repository;

import com.example.demo.models.Pasajero;
import com.example.demo.models.Pasaporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PassportRepositoryTest {

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Pasaporte p1, p2, p3, p4, p5;
    private Pasajero pasajero1, pasajero2;

    @BeforeEach
    void setUp() {
        // Crear pasaportes sin pasajero
        p1 = new Pasaporte();
        p1.setNumber("ABC123");
        entityManager.persist(p1);

        p2 = new Pasaporte();
        p2.setNumber("XYZ789");
        entityManager.persist(p2);

        p3 = new Pasaporte();
        p3.setNumber("ABC456");
        entityManager.persist(p3);

        // Crear pasaportes que tengan pasajero
        p4 = new Pasaporte();
        p4.setNumber("DEF234");
        entityManager.persist(p4);

        p5 = new Pasaporte();
        p5.setNumber("GHI789");
        entityManager.persist(p5);

        // Crear pasajeros y asignarles pasaportes
        pasajero1 = new Pasajero();
        pasajero1.setPasaporte(p4);
        entityManager.persist(pasajero1);

        pasajero2 = new Pasajero();
        pasajero2.setPasaporte(p5);
        entityManager.persist(pasajero2);

        entityManager.flush();
    }

    @Test
    void findByNumber() {
        List<Pasaporte> result = passportRepository.findByNumber("ABC123");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getNumber());
    }

    @Test
    void findByNumberStartingWith() {
        List<Pasaporte> result = passportRepository.findByNumberStartingWith("ABC");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByNumberEndingWith() {
        List<Pasaporte> result = passportRepository.findByNumberEndingWith("789");
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findByNumberContaining() {
        List<Pasaporte> result = passportRepository.findByNumberContaining("C1");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC123", result.get(0).getNumber());
    }

    @Test
    void findAllByOrderByNumberAsc() {
        List<Pasaporte> result = passportRepository.findAllByOrderByNumberAsc();
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.get(0).getNumber().compareTo(result.get(1).getNumber()) <= 0);
    }

    @Test
    void findAllByOrderByNumberDesc() {
        List<Pasaporte> result = passportRepository.findAllByOrderByNumberDesc();
        assertNotNull(result);
        assertEquals(5, result.size());
        assertTrue(result.get(0).getNumber().compareTo(result.get(1).getNumber()) >= 0);
    }

    @Test
    void findByPasajero() {
        Optional<Pasaporte> result = passportRepository.findByPasajero(pasajero1);
        assertTrue(result.isPresent());
        assertEquals("DEF234", result.get().getNumber());
    }

    @Test
    void findByPasajeroIsNull() {
        List<Pasaporte> result = passportRepository.findByPasajeroIsNull();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void findTop10ByOrderByNumberAsc() {
        List<Pasaporte> result = passportRepository.findTop10ByOrderByNumberAsc();
        assertNotNull(result);

        assertEquals(5, result.size());

        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getNumber().compareTo(result.get(i+1).getNumber()) <= 0);
        }
    }
}