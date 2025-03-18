package com.example.demo.repository;

import com.example.demo.models.Aereolinea;
import com.example.demo.models.Vuelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AirlineRepositoryTest {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Aereolinea airline1, airline2, airline3;
    private Vuelo vuelo1, vuelo2, vuelo3;

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

        vuelo3 = new Vuelo();
        vuelo3.setUuid(UUID.randomUUID());
        vuelo3.setOrigin("Bilbao");
        vuelo3.setDestiny("Barcelona");
        vuelo3 = entityManager.persistAndFlush(vuelo3);

        // Crear aerolínea con dos vuelos
        airline1 = new Aereolinea();
        airline1.setName("AirTest");
        airline1.getVuelos().add(vuelo1);
        airline1.getVuelos().add(vuelo2);
        airline1 = entityManager.persistAndFlush(airline1);

        // Crear aerolínea con un vuelo
        airline2 = new Aereolinea();
        airline2.setName("FlyHigh");
        airline2.getVuelos().add(vuelo3);
        airline2 = entityManager.persistAndFlush(airline2);

        // Crear aerolínea sin vuelos
        airline3 = new Aereolinea();
        airline3.setName("NoFlightAir");
        airline3 = entityManager.persistAndFlush(airline3);
    }

    @Test
    void findByName() {
        List<Aereolinea> result = airlineRepository.findByName("AirTest");
        assertEquals(1, result.size());
        assertEquals(airline1.getId(), result.get(0).getId());
    }

    @Test
    void findByNameContainingIgnoreCase() {
        List<Aereolinea> result = airlineRepository.findByNameContainingIgnoreCase("air");
        // Sólo "AirTest" y "NoFlightAir" contienen "air"
        assertEquals(2, result.size());
    }

    @Test
    void findAllByOrderByNameAsc() {
        List<Aereolinea> result = airlineRepository.findAllByOrderByNameAsc();
        assertEquals(3, result.size());
        // Verifica que la lista esté ordenada alfabéticamente
        assertTrue(result.get(0).getName().compareTo(result.get(1).getName()) <= 0);
        assertTrue(result.get(1).getName().compareTo(result.get(2).getName()) <= 0);
    }

    @Test
    void findByVuelosSize() {
        // airline1 tiene 2 vuelos
        List<Aereolinea> result = airlineRepository.findByVuelosSize(2);
        assertEquals(1, result.size());
        assertEquals(airline1.getId(), result.get(0).getId());
    }

    @Test
    void findByVuelosNotEmpty() {
        List<Aereolinea> result = airlineRepository.findByVuelosNotEmpty();
        // airline1 y airline2 tienen vuelos
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> a.getId().equals(airline1.getId())));
        assertTrue(result.stream().anyMatch(a -> a.getId().equals(airline2.getId())));
    }

    @Test
    void findByNameStartsWith() {
        // Buscamos aerolíneas cuyos nombres comiencen con "Fly"
        List<Aereolinea> result = airlineRepository.findByNameStartsWith("Fly");
        assertEquals(1, result.size());
        assertEquals(airline2.getId(), result.get(0).getId());
    }

    @Test
    void findByVuelosEmpty() {
        List<Aereolinea> result = airlineRepository.findByVuelosEmpty();
        // Solo airline3 no tiene vuelos
        assertEquals(1, result.size());
        assertEquals(airline3.getId(), result.get(0).getId());
    }

    @Test
    void findTopNByOrderByNameAsc() {
        // Se solicita un listado paginado con las dos primeras aerolíneas ordenadas alfabéticamente
        List<Aereolinea> result = airlineRepository.findTopNByOrderByNameAsc(PageRequest.of(0, 2));
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().compareTo(result.get(1).getName()) <= 0);
    }

    @Test
    void findByNameAndVuelosGreaterThan() {
        // airline1 tiene 2 vuelos, se solicita las aerolíneas con nombre "AirTest" y vuelos mayor que 1
        List<Aereolinea> result = airlineRepository.findByNameAndVuelosGreaterThan("AirTest", 1);
        assertEquals(1, result.size());
        assertEquals(airline1.getId(), result.get(0).getId());
    }

    @Test
    void findByVuelosDestiny() {
        // Se buscan aerolíneas que tengan al menos un vuelo cuyo destino contenga "Barcelona"
        List<Aereolinea> result = airlineRepository.findByVuelosDestiny("Barcelona");
        // airline1 tiene vuelo1 (destino Barcelona) y airline2 tiene vuelo3 (destino Barcelona)
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(a -> a.getId().equals(airline1.getId())));
        assertTrue(result.stream().anyMatch(a -> a.getId().equals(airline2.getId())));
    }
}